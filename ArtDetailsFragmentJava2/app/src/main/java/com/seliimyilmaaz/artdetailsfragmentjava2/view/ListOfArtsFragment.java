package com.seliimyilmaaz.artdetailsfragmentjava2.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seliimyilmaaz.artdetailsfragmentjava2.R;
import com.seliimyilmaaz.artdetailsfragmentjava2.adepter.ListAdepter;
import com.seliimyilmaaz.artdetailsfragmentjava2.database.ArtDao;
import com.seliimyilmaaz.artdetailsfragmentjava2.database.ArtDatabase;
import com.seliimyilmaaz.artdetailsfragmentjava2.databinding.FragmentListOfArtsBinding;
import com.seliimyilmaaz.artdetailsfragmentjava2.model.Art;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListOfArtsFragment extends Fragment {

    ArtDao artDao;
    ArtDatabase artDatabase;
    private ListAdepter adepter;
    private FragmentListOfArtsBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ListOfArtsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artDatabase = Room.databaseBuilder(requireContext(),ArtDatabase.class,"Arts").build();
        artDao = artDatabase.artDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListOfArtsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewArt.setLayoutManager(layoutManager);
        getData();
    }

    private void getData() {

        disposable.add(artDao.getArtById()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ListOfArtsFragment.this::handleResponse));

    }

    private void handleResponse(List<Art> art) {
        binding.recyclerViewArt.setLayoutManager(new LinearLayoutManager(requireContext()));
        adepter = new ListAdepter(art);
        binding.recyclerViewArt.setAdapter(adepter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }
}
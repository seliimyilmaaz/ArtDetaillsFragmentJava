package com.seliimyilmaaz.artdetailsfragmentjava2.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.seliimyilmaaz.artdetailsfragmentjava2.R;
import com.seliimyilmaaz.artdetailsfragmentjava2.database.ArtDao;
import com.seliimyilmaaz.artdetailsfragmentjava2.database.ArtDatabase;
import com.seliimyilmaaz.artdetailsfragmentjava2.databinding.FragmentDetailsOfArtsBinding;
import com.seliimyilmaaz.artdetailsfragmentjava2.model.Art;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DetailsOfArts extends Fragment {

    private FragmentDetailsOfArtsBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Bitmap selectedImageBitmap;
    private CompositeDisposable disposable = new CompositeDisposable();
    ArtDao artDao;
    ArtDatabase artDatabase;
    SQLiteDatabase database;
    Art artFromMain;
    String info = "";


    public DetailsOfArts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerLauncher();

        artDatabase = Room.databaseBuilder(requireContext(),ArtDatabase.class,"Arts").build();
        artDao = artDatabase.artDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailsOfArtsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE,null);

        if(getArguments() != null){
            info = DetailsOfArtsArgs.fromBundle(getArguments()).getInfo();
        }else {
            info = "new";
        }

        binding.imgSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage(view);
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo(view);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(view);
            }
        });

        if(info.equals("new")){
            binding.artistName.setText("");
            binding.txtArtName.setText("");
            binding.txtYearOfArt.setText("");
            binding.btnSave.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.INVISIBLE);

            binding.imgSelected.setImageResource(R.drawable.selectedimages);

        }else{
            int id = DetailsOfArtsArgs.fromBundle(getArguments()).getArtId();
            binding.btnSave.setVisibility(View.INVISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);

            disposable.add(artDao.getAll(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(DetailsOfArts.this::handleResposeWithOldArt));
        }
    }

    private void handleResposeWithOldArt(Art art) {
        artFromMain = art;
        binding.txtArtName.setText(art.artName);
        binding.artistName.setText(art.artistName);
        binding.txtYearOfArt.setText(art.yearOfArt);

        Bitmap bitmap = BitmapFactory.decodeByteArray(art.image,0,art.image.length);
        binding.imgSelected.setImageBitmap(bitmap);
    }

    private void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent.getData() != null){
                        Uri getImageData = intent.getData();

                        try {
                            if(Build.VERSION.SDK_INT >= 28){
                                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(),getImageData);
                                selectedImageBitmap =ImageDecoder.decodeBitmap(source);
                            }else {
                                selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),getImageData);
                            }
                            binding.imgSelected.setImageBitmap(selectedImageBitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);
                }else {
                    Toast.makeText(getActivity(), "Permission Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void selectedImage(View view) {

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                Snackbar.make(view,"Permission Needed", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }

    }

    public void deleteInfo(View view){

        disposable.add(artDao.delete(artFromMain)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DetailsOfArts.this::handleResponse));

    }

    public void saveInfo(View view){

        String artName = binding.txtArtName.getText().toString();
        String artistName = binding.artistName.getText().toString();
        String yearOfArt = binding.txtYearOfArt.getText().toString();

        Bitmap selectedImg = makeSmallerImage(selectedImageBitmap,300);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImg.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);

        byte[] byteList = byteArrayOutputStream.toByteArray();

        Art art = new Art(artName,artistName,yearOfArt,byteList);

        disposable.add(artDao.insert(art)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DetailsOfArts.this::handleResponse));

    }

    public void handleResponse(){
        NavDirections navDirections = DetailsOfArtsDirections.actionDetailsOfArtsToListOfArtsFragment();
        Navigation.findNavController(requireView()).navigate(navDirections);
    }

    public Bitmap makeSmallerImage(Bitmap image,int maxSize){

        float height = image.getHeight();
        float width = image.getWidth();

        float ratio = width/height;

        if(ratio > 1){
            width  = (float) maxSize;
            height = maxSize / ratio;
        }else{
            height = (float) maxSize;
            width = maxSize * ratio;
        }

        return Bitmap.createScaledBitmap(image,(int)width,(int)height,true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        disposable.clear();
    }
}
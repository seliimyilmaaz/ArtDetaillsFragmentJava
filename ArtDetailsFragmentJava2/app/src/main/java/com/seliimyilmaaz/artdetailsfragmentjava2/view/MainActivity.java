package com.seliimyilmaaz.artdetailsfragmentjava2.view;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seliimyilmaaz.artdetailsfragmentjava2.R;
import com.seliimyilmaaz.artdetailsfragmentjava2.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_art_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.addArt){
            ListOfArtsFragmentDirections.ActionListOfArtsFragmentToDetailsOfArts actions
                    = ListOfArtsFragmentDirections.actionListOfArtsFragmentToDetailsOfArts("new");
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(actions);
        }

        return super.onOptionsItemSelected(item);

    }
}
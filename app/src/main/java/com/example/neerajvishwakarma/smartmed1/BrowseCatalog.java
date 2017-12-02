package com.example.neerajvishwakarma.smartmed1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BrowseCatalog extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<MedEnter> medList = new ArrayList<>();
    ArrayList<MedEnter> medListClone;
    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_catalog);
        medListClone = new  ArrayList<>(medList);
        medicineAdapter = new MedicineAdapter(medList);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        medicineAdapter = new MedicineAdapter(medList);
        recyclerView.setAdapter(medicineAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder>
    {

        public MedicineAdapter(ArrayList<MedEnter> medList){

        }

        @Override
        public MedicineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
            return new MedicineAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MedicineAdapter.MyViewHolder holder, int position) {
            MedEnter medEnter = medList.get(position);
            holder.nameI.setText(medEnter.getName());

        }

        @Override
        public int getItemCount() {
            return medList.size();
        }

        public void filtter(String charText) {
            medList.clear();
            if(charText.isEmpty()){
                medList.addAll(medListClone);

            }else
            {
                charText = charText.toLowerCase();
                for(MedEnter item : medListClone){
                    if(item.getName().toLowerCase().contains(charText)){
                        medList.add(item);
                    }
                }
            }
           notifyDataSetChanged();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView nameI;
            RelativeLayout baseLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                nameI = (TextView) itemView.findViewById(R.id.tname);
                baseLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        MedEnter medEnter = medList.get(position);
                        Intent i = new Intent(BrowseCatalog.this,MedInfo.class);
                        //i.putExtra("medToUpdate" ,MedEnter);
                        //startActivityForResult(i,UPDATE_MED);
                    }
                });

            }
        }
    }
}

package com.example.phoebemanning.capstone.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.Activities.ProductActivity;
import com.example.phoebemanning.capstone.Models.Nutrient_Models.Nutrients;
import com.example.phoebemanning.capstone.Models.Scan;
import com.example.phoebemanning.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterScans extends RecyclerView.Adapter<RecyclerAdapterScans.MyViewHolder> {

    private ArrayList<Scan> scans;
    private Context context;
    private Dialog myDialog;

    public RecyclerAdapterScans(ArrayList<Scan> scans, Context context){
        this.scans = scans;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scan_item_layout, viewGroup, false);
        final MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    private void removeFromDb(final String scanNameDelete) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scans").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Scan> list = new ArrayList<Scan>();
                String key = null;

                for(DataSnapshot s : dataSnapshot.getChildren()){
                    Scan scan = s.getValue(Scan.class);
                    if(scan.getProductName().equals(scanNameDelete)){
                        key = s.getKey();
                    }
                }

                if(key != null){
                    databaseReference.child(key).removeValue();
                    myDialog.hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dialog_delete);

        String scanName = scans.get(i).getProductName();
        myViewHolder.scanName.setText(scanName.toUpperCase());

        myViewHolder.scanItemHolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int scanPos = myViewHolder.getAdapterPosition();
                final String scanName = scans.get(myViewHolder.getAdapterPosition()).getProductName();

                TextView delete = myDialog.findViewById(R.id.deleteScan);
                myDialog.show();
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scans.clear();
                        removeFromDb(scanName);
                    }
                });

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView scanName;
        RelativeLayout scanItemHolder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            scanName = itemView.findViewById(R.id.scan_item);
            scanItemHolder = itemView.findViewById(R.id.scan_item_holder);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Scan scan = scans.get(pos);
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("name", scan.getProductName());
            intent.putExtra("upc", scan.getUpcCode());
            intent.putExtra("ndbno", scan.getNdbno());
            context.startActivity(intent);
        }

    }
}

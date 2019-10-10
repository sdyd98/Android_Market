package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Category_Cpu_Activity extends AppCompatActivity {

    ImageView Category_Cpu_Back_Btn, Category_Cpu_Mymenu_Btn, Category_Cpu_Search_Icon;
    TextView Category_Name;
    static int Category_position_number;

    //리사이클러뷰 선언
    private RecyclerView Category_Item_Recycle;
    // 어뎁터 선언
    private RecyclerView.Adapter cimAdapter;
    // 레이아웃 매니저 선언
    private RecyclerView.LayoutManager LayoutManager_Category_Item_Recycle;

    // 셋팅값 선언
    static ArrayList<Item_Profile> Category_Item_Cpu = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_Gpu = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_RAM = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_MB = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_SSD = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_HDD = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_POWER = new ArrayList<>();
    static ArrayList<Item_Profile> Category_Item_COOLER = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori_cpu);

        //리사이클러뷰 매칭
        Category_Item_Recycle = findViewById(R.id.Category_Detail_Recycle);

        // 얘는 뭘까
        Category_Item_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저  getApplicationContext?? 무엇
        LayoutManager_Category_Item_Recycle = new GridLayoutManager(getApplicationContext(), 3);

        // 레이아웃 매니저 set
        Category_Item_Recycle.setLayoutManager(LayoutManager_Category_Item_Recycle);

        Category_Name = findViewById(R.id.Category_Name);
        Category_Cpu_Search_Icon = findViewById(R.id.Category_Cpu_Search_Icon);
        Category_Cpu_Back_Btn = findViewById(R.id.Category_Cpu_Back_Btn);
        Category_Cpu_Mymenu_Btn = findViewById(R.id.Category_Cpu_Mymenu_Btn);
        Category_Name.setText(getIntent().getStringExtra("Category_Name"));



        if(Category_Name.getText().toString().equals("CPU")) {
            Category_Item_Cpu.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("CPU")) {
                    Category_Item_Cpu.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_Cpu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }






        if(Category_Name.getText().toString().equals("GPU")) {
            Category_Item_Gpu.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("GPU")) {
                    Category_Item_Gpu.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_Gpu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
        }







        if(Category_Name.getText().toString().equals("RAM")) {
            Category_Item_RAM.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("RAM")) {
                    Category_Item_RAM.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_RAM, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("MB")) {
            Category_Item_MB.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("MB")) {
                    Category_Item_MB.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_MB, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("SSD")) {
            Category_Item_SSD.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("SSD")) {
                    Category_Item_SSD.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_SSD, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("HDD")) {
            Category_Item_HDD.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("HDD")) {
                    Category_Item_HDD.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_HDD, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }








        if(Category_Name.getText().toString().equals("POWER")) {
            Category_Item_POWER.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("Power")) {
                    Category_Item_POWER.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_POWER, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);

        }






        if(Category_Name.getText().toString().equals("COOLER")) {
            Category_Item_COOLER.clear();
            for (int i = 0; i < Main_Activity.test1.size(); i++) {
                if (Main_Activity.test1.get(i).getCategori_Name().equals("Cooler")) {
                    Category_Item_COOLER.add(Main_Activity.test1.get(i));
                }
            }
                cimAdapter = new MyAdapter(Category_Item_COOLER, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if (obj != null) {
                            int position = (int) obj;
                            ((MyAdapter) cimAdapter).getData(position);
                            Category_position_number = position;
                            Intent intent = new Intent(Category_Cpu_Activity.this, Buy_Activity.class);
                            intent.putExtra("Item_Name", ((MyAdapter) cimAdapter).getData(position).getItem_Name());
                            intent.putExtra("Item_Price", ((MyAdapter) cimAdapter).getData(position).getItem_Price());
                            intent.putExtra("Item_Img", ((MyAdapter) cimAdapter).getData(position).getItem_Img());
                            intent.putExtra("Item_Detail", ((MyAdapter) cimAdapter).getData(position).getItem_Detail());
                            intent.putExtra("Item_Categori", ((MyAdapter) cimAdapter).getData(position).getCategori_Name());
                            //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                            // 하나씩 넘길수도 있고
                            // 한번에 넘길수도 있다 (Serializable)
                            startActivity(intent);
                        }
                    }
                });
                Category_Item_Recycle.setAdapter(cimAdapter);
            }



        Category_Cpu_Mymenu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Cpu_Activity.this, Mymenu_Activity.class);
                startActivity(intent);
            }
        });

        Category_Cpu_Search_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_Cpu_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        Category_Cpu_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cimAdapter.notifyDataSetChanged();
        Toast.makeText(this, "onReStart 호출 됨", Toast.LENGTH_LONG).show();
    }
}
package com.example.Tarea2_2_Aplicacion_ListView_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.Tarea2_2_Aplicacion_ListView_api.api.UsuariosApi;
import com.example.Tarea2_2_Aplicacion_ListView_api.Tablas.Usuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusquedaActivity extends AppCompatActivity {

    ListView listdeapersonas;
    ArrayList<String> titulo = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    EditText txtid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        txtid = (EditText) findViewById(R.id.txtid);

        txtid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ObtenerUsuario(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titulo);
        listdeapersonas = (ListView) findViewById(R.id.posts);
        listdeapersonas.setAdapter(arrayAdapter);
    }

    private void ObtenerUsuario(String texto)
    {
        if (titulo.size()>0)
        {
            titulo.remove(0);
        }
        arrayAdapter.notifyDataSetChanged();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);

        Call<Usuarios> calllista = usuariosApi.getUsuario(texto);
        calllista.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response)
            {
                Log.i(response.body().getTitle(), "onResponse");
                titulo.add(response.body().getTitle());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {

            }
        });
    }
    private void obtenerListaPersonas()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);

        Call<List<Usuarios>> calllista = usuariosApi.getUsuarios();

        calllista.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response)
            {
                for(Usuarios usuarios : response.body())
                {
                    Log.i(usuarios.getTitle(), "onResponse");
                    titulo.add(usuarios.getTitle());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                t.getMessage();
            }
        });
    }


}
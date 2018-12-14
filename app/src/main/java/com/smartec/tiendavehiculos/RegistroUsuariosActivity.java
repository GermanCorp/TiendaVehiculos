package com.smartec.tiendavehiculos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartec.tiendavehiculos.entidades.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistroUsuariosActivity extends AppCompatActivity {

    String path;
    private final String CARPETA_PRINCIPAL ="misImagenesApp/";
    private final String CARPETA_IMAGEN ="imagenes";
    private final String DIRECTORIO_IMAGEN= CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private final int COD_SELECCIONA = 10;
    private final int COD_FOTO = 20;
    Bitmap bitmap;
    File fileImagen;
    private ProgressDialog progressDialog;
    ImageView fotoUsuario;
    TextInputEditText campoNombres, campoApellidos,campoDireccion, campoEmail, campoTelefono, campoCelular,campoNombreUsuario, campoContrasenia;
    Button botonRegistrar;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private String funcionBoton = "Guardar";
    Usuario usuario = new Usuario();

    public static String nombresU, apellidosU, direccionU, emailU, telefonoU, celularU, nombreUsuarioU, contrasenaU, foto = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);

        fotoUsuario = (ImageView)findViewById(R.id.imageView);
        campoNombres = (TextInputEditText)findViewById(R.id.textNombres);
        campoApellidos= (TextInputEditText)findViewById(R.id.textApellidos);
        campoDireccion =  (TextInputEditText)findViewById(R.id.textDireccion);
        campoEmail = (TextInputEditText)findViewById(R.id.textEmail);
        campoCelular = (TextInputEditText)findViewById(R.id.textCelular);
        campoContrasenia = (TextInputEditText)findViewById(R.id.textContrasenia);
        campoTelefono = (TextInputEditText)findViewById(R.id.textTelefono);
        campoNombreUsuario = (TextInputEditText)findViewById(R.id.textUsuario);
        botonRegistrar = (Button) findViewById(R.id.buttonRegistrar);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        funcionBoton = getIntent().getExtras().getString("funcion");

        if(funcionBoton.equals("Guardar")  ){
            botonRegistrar.setText("Registrar");
        }else {
            botonRegistrar.setText("Editar");
            campoNombres.setText(nombresU);
            campoApellidos.setText(apellidosU);
            campoDireccion.setText(direccionU);
            campoCelular.setText(celularU);
            campoNombreUsuario.setText(nombreUsuarioU);
            campoTelefono.setText(telefonoU);
            campoEmail.setText(emailU);
            campoContrasenia.setText(contrasenaU);

            cargarWebServiceImagen(foto);
        }


        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(funcionBoton.equals("Guardar")){
                    registrarUsuario();

                }else{
                    actualizarUsuario();
                }

            }
        });

        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

    }


    private void cargarWebServiceImagen(String urlImagen) {
        urlImagen = urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                fotoUsuario.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        requestQueue.add(imageRequest);
    }

    private void actualizarUsuario() {

        String url = ServerConfig.URL_BASE+"actualizarUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                campoNombres.setText("");
                campoApellidos.setText("");
                campoDireccion.setText("");
                campoTelefono.setText("");
                campoCelular.setText("");
                campoEmail.setText("");
                campoNombreUsuario.setText("");
                campoContrasenia.setText("");
                fotoUsuario.setImageResource(R.mipmap.ic_launcher);
                Toast.makeText(getApplicationContext(), "Registro Exitoso"+response, Toast.LENGTH_SHORT).show();


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String nombres =campoNombres.getText().toString();
                String apellidos = campoApellidos.getText().toString();
                String email = campoEmail.getText().toString();
                String telefono = campoTelefono.getText().toString();
                String celular = campoCelular.getText().toString();
                String nombreUsuario = campoNombreUsuario.getText().toString();
                String contrasena =campoContrasenia.getText().toString();
                String direccion = campoDireccion.getText().toString();
                String imagen = convertirImagenString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("id",usuario.getId().toString());;
                parametros.put("nombres",nombres);
                parametros.put("apellidos",apellidos);
                parametros.put("telefono",telefono);
                parametros.put("celular",celular);
                parametros.put("email",email);
                parametros.put("nombreUsuario",nombreUsuario);
                parametros.put("contrasena",contrasena);
                parametros.put("direccion",direccion);
                parametros.put("imagen",imagen);

                return parametros;

            }

        };
        //progressDialog.hide();
        requestQueue.add(stringRequest );


    }


    private void registrarUsuario() {
        /*progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registrando...");
        progressDialog.show();*/

        String url = ServerConfig.URL_BASE+"registroUsuario.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    campoNombres.setText("");
                    campoApellidos.setText("");
                    campoDireccion.setText("");
                    campoTelefono.setText("");
                    campoCelular.setText("");
                    campoEmail.setText("");
                    campoNombreUsuario.setText("");
                    campoContrasenia.setText("");
                    fotoUsuario.setImageResource(R.mipmap.ic_launcher);
                    Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombres =campoNombres.getText().toString();
                String apellidos = campoApellidos.getText().toString();
                String email = campoEmail.getText().toString();
                String telefono = campoTelefono.getText().toString();
                String celular = campoCelular.getText().toString();
                String nombreUsuario = campoNombreUsuario.getText().toString();
                String contrasena =campoContrasenia.getText().toString();
                String direccion = campoDireccion.getText().toString();
                String imagen = convertirImagenString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombres",nombres);
                parametros.put("apellidos",apellidos);
                parametros.put("telefono",telefono);
                parametros.put("celular",celular);
                parametros.put("email",email);
                parametros.put("nombreUsuario",nombreUsuario);
                parametros.put("contrasena",contrasena);
                parametros.put("direccion",direccion);
                parametros.put("imagen",imagen);

                return parametros;

            }

        };
        //progressDialog.hide();
        requestQueue.add(stringRequest );


    }


    private String convertirImagenString(Bitmap bitmap) {

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100
                ,arrayOutputStream);
        byte[] imagenByte = arrayOutputStream .toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


    private void cargarImagen(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistroUsuariosActivity.this);
        alertOpciones.setTitle("Seleccione una Opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{

                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),COD_SELECCIONA);

                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });

        alertOpciones.show();
    }

    private void tomarFotografia() {

        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        String nombre ="";

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true) {
            nombre = (System.currentTimeMillis() / 1000) + ".jpg";

        }
        path= Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+
                File.separator+nombre;

        fileImagen= new File(path);

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            String authorities = getApplicationContext().getApplicationContext().getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),authorities,fileImagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
        }

        startActivityForResult(intent,COD_FOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:

                    Uri miPath = data.getData();
                    //fotoUsuario.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                        fotoUsuario.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });

                    bitmap = BitmapFactory.decodeFile(path);
                    fotoUsuario.setImageBitmap(bitmap);
                    break;

            }

        }


    }
}

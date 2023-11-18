package com.uth.e23;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kyanogen.signatureview.SignatureView;
import com.uth.e23.database.DBPICTURE;
import com.uth.e23.models.cPhotograh;


public class NewPicture extends AppCompatActivity
{

    private DBPICTURE oDBPICTURE;

    private TextInputEditText oTextInputEditTextDescripcion;
    private MaterialButton oMaterialButtonSalvar;

    private ImageView oSignatureView;

    private Bitmap oBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signature);

        this.oTextInputEditTextDescripcion = this.findViewById(R.id.txtDescripcion);
        this.oMaterialButtonSalvar = this.findViewById(R.id.btn_salvar);
        this.oSignatureView = this.findViewById(R.id.signature_view);
        oDBPICTURE = new DBPICTURE(NewPicture.this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.oMaterialButtonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(oBitmap != null && !oTextInputEditTextDescripcion.getText().toString().isEmpty())
                {
                    cPhotograh oS = new cPhotograh();
                    oS.setDescripcion(oTextInputEditTextDescripcion.getText().toString());
                    oS.setFoto(oBitmap);
                    if(oDBPICTURE.createSignature(oS)){
                        Toast.makeText(NewPicture.this, "USUARIO GUARDA CON EXITO", Toast.LENGTH_SHORT).show();
                        oTextInputEditTextDescripcion.setText("");
                        oSignatureView.setImageDrawable(getResources().getDrawable(R.drawable.camera));
                    }else{
                        Toast.makeText(NewPicture.this, "ERROR AL GUARDAR LA FIRMA", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NewPicture.this, "EXISTEN DATOS VACIOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.oSignatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamera();
            }
        });
    }



    public void abrirCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            oBitmap = (Bitmap) extras.get("data");
            oSignatureView.setImageBitmap(oBitmap);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
package dam.macr.proyecto_macr.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.activities.LoginActivity;
import dam.macr.proyecto_macr.activities.MensajeActivity;
import dam.macr.proyecto_macr.pojos.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private Context context;
    private List<Usuario> listaUsuarios;
    private String remitente;

    public UsuarioAdapter(Context context, List<Usuario> listaUsuarios, String remitente) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
        this.remitente = remitente;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.usuario_item, parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        holder.tvUsername.setText("@" + usuario.getUsuario());
        holder.tvNombre.setText(usuario.getNombre());
        holder.tvCiudad.setText(usuario.getCiudad());

        // Nivel visual (progress bar y texto)
        holder.barraNivel.setProgress(usuario.getNivel());
        holder.tvNivelTexto.setText(getDescripcionNivel(usuario.getNivel()));

        if (usuario.getImagen() != null) {
            byte[] decodedString = Base64.decode(usuario.getImagen(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(decodedByte, 200, 200, true);
            holder.ivFotoUsuario.setImageBitmap(scaledBitmap);
        } else {
            holder.ivFotoUsuario.setImageResource(R.drawable.amigo); // Imagen por defecto
        }

        // Evento de clic sobre el ítem
        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(usuario.getNombre())
                    .setItems(new CharSequence[]{"💬  Enviar Mensaje", "❤\uFE0F  Añadir Amigo"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                // Enviar mensaje
                                Intent intent = new Intent(context, MensajeActivity.class);
                                intent.putExtra("remitente", remitente);
                                intent.putExtra("destinatario", usuario.getUsuario());
                                context.startActivity(intent);
                                break;
                            case 1:
                                Toast.makeText(context, "Solicitud de amistad enviada a " + "@" + usuario.getUsuario() + " ✅", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFotoUsuario;
        TextView tvUsername, tvNombre, tvCiudad, tvNivelTexto;
        ProgressBar barraNivel;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoUsuario = itemView.findViewById(R.id.ivFotoUsuario);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCiudad = itemView.findViewById(R.id.tvCiudad);
            tvNivelTexto = itemView.findViewById(R.id.tvNivelTexto);
            barraNivel = itemView.findViewById(R.id.barraNivel);
        }
    }

    private String getDescripcionNivel(int nivel) {
        switch (nivel) {
            case 1: return "PRINCIPIANTE";
            case 2: return "AMATEUR";
            case 3: return "AVANZADO";
            case 4: return "EXPERTO";
            case 5: return "MÁSTER";
            default: return "";
        }
    }

    public void actualizarLista(List<Usuario> nuevaLista) {
        this.listaUsuarios = nuevaLista;
        notifyDataSetChanged();
    }
}

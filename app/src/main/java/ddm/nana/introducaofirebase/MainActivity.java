package ddm.nana.introducaofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    // Atributos que representam os objetos da interface gráfica:
    private EditText txtNome;
    private EditText txtSobrenome;
    private EditText txtTelefone;
    private Button btnEnviar;
    private ListView listaContatos;

    // Criando uma referência para o nó "usuarios" da árvore.
    // Isto é, a referência vai "apontar" para o nó "usuarios".
    // Se o nó não existir, ele cria.
    private DatabaseReference contatos = BD.child("contatos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ligando os atributos com a interface gráfica:
        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtTelefone = findViewById( R.id.txtTelefone);
        btnEnviar = findViewById( R.id.btnSalvar);

        // Criando o escutador do botão enviar
        btnEnviar.setOnClickListener( new EscutadorBotao() );

        // Criando uma referência para o nó "usuarios" da árvore.
        // Isto é, a referência vai "apontar" para o nó "usuarios".
        // Se o nó não existir, ele cria.

        // Atente para o significado do comando abaixo:
        // "O nó usuarios é filho (child) do BD.
        // Como BD aponta para a RAIZ, "usuarios" está debaixo
        // (é filho) da RAIZ.
        //DatabaseReference usuarios = BD.child("usuarios");

        // Vamos criar dois objetos da classe Usuario:
        //Usuario u1 = new Usuario("Carlos", "Pereira", 48);
        //Usuario u2 = new Usuario("Darth", "Vader", 50);

        // Vamos gravar o objeto "u1" como filho (child) do nó "001",
        // e o objeto "u2" como filho (child) do nó "002",
        // ambos como nós filhos do nó "usuarios":
        //usuarios.child( "001" ).setValue( u1 );
        //usuarios.child( "002" ).setValue( u2 );
    }

    // classe do escutador do botão
    private class EscutadorBotao implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // variáveis para os dados digitados nas caixas
            String nome, sobren, tele;

            // Pegando os dados nas caixas.
            // ATENÇÃO!! não vamos fazer filtros de erros!
            nome = txtNome.getText().toString();
            sobren = txtSobrenome.getText().toString();
            tele = txtTelefone.getText().toString();

            // Criando o objeto Usuario, com os dados acima
            Usuario u = new Usuario( nome, sobren, tele );

            // Gerando chave aleatória
            String chave = contatos.push().getKey();

            // Vamos ter apenas um nó, "dados", então podemos gravar direto
            contatos.child(chave).setValue( u );

            // Criando e associando o escutador do Firebase para este nó.
            // OBS!!! Apenas para uma única leitura.
            // Ou seja, cada vez que apertar o botão, cria o escutador, associa,
            // ele faz seu trabalho, e "morre".
            contatos.addListenerForSingleValueEvent( new EscutadorFirebase() );
        }
    }

    // Classe interna do escutador do Firebase:
    private class EscutadorFirebase implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            // Testando se veio dados no dataSnapshot:
            if ( dataSnapshot.exists() ) {

                // Variáveis auxiliares...
                String n, s, t;

                // OK, temos dados!
                // Vamos "varrer" os filhos deste dataSnapshot:
                for ( DataSnapshot datasnapContatos : dataSnapshot.getChildren() ) {

                    // A variável "dataSnapUsuario" tem um dataSnapshot com apenas 1 único usuário lido.
                    // Vamos extrair esse usuário!
                    Usuario u = datasnapContatos.getValue( Usuario.class );

                    // Agora é só exibir os dados deste usuário no toast...
                    n = u.getNome();
                    s = u.getSobrenome();
                    t = u.getTelefone();
                    Toast.makeText(MainActivity.this, "Nome: "+n+"\nSobrenome: "+s+"\nTelefone: "+t, Toast.LENGTH_SHORT).show();
                }
            }
        }
        // Não trabalharemos com este método...
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

}
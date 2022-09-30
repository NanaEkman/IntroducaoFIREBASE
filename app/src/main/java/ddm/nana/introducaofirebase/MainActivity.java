package ddm.nana.introducaofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    // Atributos que representam os objetos da interface gráfica:
    private EditText txtNome;
    private EditText txtSobrenome;
    private EditText txtIdade;
    private Button btnEnviar;
    private TextView lblNome;
    private TextView lblSobrenome;
    private TextView lblIdade;

    // Contador para os ids dos usuarios
    private int cont = 000;

    // Criando uma referência para o nó "usuarios" da árvore.
    // Isto é, a referência vai "apontar" para o nó "usuarios".
    // Se o nó não existir, ele cria.
    private DatabaseReference usuarios = BD.child("usuarios2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ligando os atributos com a interface gráfica:
        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtIdade = findViewById( R.id.txtIdade );
        btnEnviar = findViewById( R.id.btnEnviar );
        lblNome = findViewById( R.id.lblNome );
        lblSobrenome = findViewById( R.id.lblSobrenome );
        lblIdade = findViewById( R.id.lblIdade );

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
            String nome, sobren;
            int idade;

            // Pegando os dados nas caixas.
            // ATENÇÃO!! não vamos fazer filtros de erros!
            nome = txtNome.getText().toString();
            sobren = txtSobrenome.getText().toString();
            idade = Integer.parseInt( txtIdade.getText().toString() );

            // Criando o objeto Usuario, com os dados acima
            Usuario u = new Usuario( nome, sobren, idade );

            // Vamos ter apenas um nó, "dados", então podemos gravar direto
            usuarios.child(String.valueOf((cont++))).setValue( u );
        }
    }

}
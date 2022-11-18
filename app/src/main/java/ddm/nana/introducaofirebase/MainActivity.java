package ddm.nana.introducaofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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
    //private DatabaseReference contatos = BD.child("contatos");

    // Variável para usar o adapter do firebase
    private ContatosAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference contatos = BD.child("contatos");

        // Ligando os atributos com a interface gráfica:
        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtTelefone = findViewById( R.id.txtTelefone);
        btnEnviar = findViewById( R.id.btnSalvar);
        listaContatos = findViewById( R.id.listaContatos);

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

        // Criando objeto com parâmetros para o adapter:
        FirebaseListOptions<Contato> options = new FirebaseListOptions.Builder<Contato>()
                .setLayout(R.layout.contato)
                .setQuery(contatos, Contato.class)
                .setLifecycleOwner( this )
                .build();

        // Criando o objeto adapter (usando os parâmetros criados acima):
        adapter = new ContatosAdapter(options);

        // Colocando o adapter no ListView:
        listaContatos.setAdapter(adapter);

        // Criando e associando o escutador de cliques na lista:
        listaContatos.setOnItemClickListener( new EscutadorCliqueLista() );

    }

    // classe do escutador do botão
    private class EscutadorBotao implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // Referência para o nó principal deste exemplo:
            DatabaseReference contatos = BD.child("contatos");

            // Pegando os dados nas caixas.
            // ATENÇÃO!! não vamos fazer filtros de erros!
            String nome = txtNome.getText().toString();
            String sobrenome = txtSobrenome.getText().toString();
            String telefone = txtTelefone.getText().toString();

            // Gerando chave aleatória
            String chave = contatos.push().getKey();

            // Criando o objeto Usuario, com os dados acima
            Contato u = new Contato( chave, nome, sobrenome, telefone );

            // Vamos ter apenas um nó, "dados", então podemos gravar direto
            contatos.child(chave).setValue( u );

            // Criando e associando o escutador do Firebase para este nó.
            // OBS!!! Apenas para uma única leitura.
            // Ou seja, cada vez que apertar o botão, cria o escutador, associa,
            // ele faz seu trabalho, e "morre".
            //contatos.addListenerForSingleValueEvent( new EscutadorFirebase() );

            // Limpando as caixas, para próxima digitação:
            txtNome.setText("");
            txtSobrenome.setText("");
            txtTelefone.setText("");

            // Colocando o cursor (o foco) de volta na caixa nome:
            txtNome.requestFocus();
        }
    }

    // Classe interna do escutador do Firebase:
    /*private class EscutadorFirebase implements ValueEventListener {

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
                    Contato u = datasnapContatos.getValue( Contato.class );

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
    }*/

    // Classe interna do adapter do Firebase:
    private class ContatosAdapter extends FirebaseListAdapter<Contato> {

        // Construtor: receita de bolo!
        // Recebe um objeto do tipo FirebaseListOptions,
        // e passa ele para o construtor da classe mãe.
        // Veremos como criar este objeto mais a frente.
        public ContatosAdapter(FirebaseListOptions options) {
            super( options );
        }

        // Método que coloca dados ("povoa") a View (o desenho) do item da lista.
        // Recebe o objeto com os dados (vindos do Firebase), e a View já inflada.
        // Basta acessarmos os dados (nome e email) e colocarmos nos objetos corretos
        // dentro da View.
        @Override
        protected void populateView(View v, Contato c, int position) {

            // Acessa os objetos gráficos dentro do desenho do item da lista:
            TextView lblNome = v.findViewById( R.id.lblNome );
            TextView lblSobrenome = v.findViewById( R.id.lblSobrenome );
            TextView lblTelefone = v.findViewById( R.id.lblTelefone );

            // Coloca dados do objeto Contato (c) nesses objetos gráficos:
            lblNome.setText( c.getNome() );
            lblSobrenome.setText( c.getSobrenome() );
            lblTelefone.setText( c.getTelefone() );
        }
    }

    // Classe interna do escutador de clicks na lista:
    private class EscutadorCliqueLista implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            // Recuperando o objeto que está na posição "i" da lista:
            Contato c = adapter.getItem(i);

            // Exibindo seus dados em um Toast:
            Toast.makeText(getApplicationContext(), c.getNome() +
                            " " + c.getSobrenome() + " - " + c.getTelefone(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
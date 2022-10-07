package ddm.nana.introducaofirebase;

public class Usuario {
    // Atributos
    private String nome;
    private String sobrenome;
    private String telefone;

    // Construtor
    public Usuario ( String n, String s, String t ) {
        nome = n; sobrenome = s; telefone = t;
    }

    // É obrigatório ter um construtor vazio!!!!
    public Usuario() {
    }

    // É obrigatório ter todos os getters e setters!!
    // Android Studio pode criar automaticamente pra você.
    // menu Code | Generate | Getter and Setter
    // Marca todos os atributos, clica OK.
    public String getNome() { return nome; }

    public void setNome(String n) { this.nome = n; }

    public String getSobrenome() { return sobrenome; }

    public void setSobrenome(String s) { this.sobrenome = s; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String t) { this.telefone = t; }
}

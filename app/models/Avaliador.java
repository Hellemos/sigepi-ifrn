package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


/**
 * Entidade responsável pela representação dos usuários do sistema
 * no banco de dados.
 * 
 * @author alessandro
 */
@Entity
public class Avaliador extends Model {
	@Id
	public Long id;
	
	@Required(message="O campo deve ser preenchido.")
	@Column(unique=true)
	@MinLength(value=4,message="A senha deve conter no mínimo 4 caracteres.")
	public String login;
	
	@Required(message="O campo deve ser preenchido.")
	public String senha;
	
	@Required(message="O campo deve ser preenchido.")
	public String nome;
	
	@Required(message="O campo deve ser preenchido.")
	@Column(unique=true)
	@Email(message="Você deve informar um email válido.")
	public String email;
	
	public String chaveRedefinicaoSenha;
	
	
	
	public boolean isProfessor;
	public boolean isAtivo;

	public Avaliador() {
		this.chaveRedefinicaoSenha = null;
		
		this.isAtivo = false;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChaveRedefinicaoSenha() {
		return chaveRedefinicaoSenha;
	}

	public void setChaveRedefinicaoSenha(String chaveRedefinicaoSenha) {
		this.chaveRedefinicaoSenha = chaveRedefinicaoSenha;
	}

	public boolean isProfessor() {
		return isProfessor;
	}

	public void setProfessor(boolean isProfessor) {
		this.isProfessor = isProfessor;
	}

	public boolean isAtivo() {
		return isAtivo;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
	}

	public static Finder<Long, Avaliador> find = new Finder<Long, Avaliador>(Long.class, Avaliador.class);
	
	/**
	 * Método <strong>Save()</strong> sobrescrito para criptografar 
	 * a senha do usuário antes de enviar para o banco de dados.
	 * <br/>
	 * Método de Criptografia: MD5
	 */
	@Override
	public void save() {
		try {
			this.senha = criptografarSenha(senha);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		super.save();
	}
	
	/**
     * Método responsável por verificar se o usuário e senha conferem no banco de dados.
	 * @throws NoSuchAlgorithmException 
     */
    public static Avaliador autenticar(String login, String senha) throws NoSuchAlgorithmException {
        return find.where()
            .eq("login", login)
            .eq("senha", criptografarSenha(senha))
            .findUnique();
    }
    
    /**
     * Método responsável pela criptografia em MD5 da senha do usuário 
     * no momento em que o objeto é persistido no banco de dados.
     * 
     * @param senha
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String criptografarSenha(String senha) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(senha.getBytes(), 0, senha.length());
		return new BigInteger(1, digest.digest()).toString(16);
	}
}
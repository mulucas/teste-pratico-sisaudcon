package br.com.login.dao;

import br.com.login.model.Usuario;
import br.com.login.util.CriptografiaUtil;

import java.sql.*;

public class UsuarioDAO {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/fungestao";
    private static final String USER = "postgres";
    private static final String PASS = "123456";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean autenticar(String email, String senhaPura) {
        
        String senhaHash = CriptografiaUtil.sha256(senhaPura);
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senhaHash);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
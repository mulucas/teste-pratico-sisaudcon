package br.com.login.dao;

import br.com.login.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/fungestao";
    private static final String USER = "postgres";
    private static final String PASS = "123456";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, data_admissao, salario, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setDate(2, Date.valueOf(funcionario.getDataAdmissao()));
            stmt.setBigDecimal(3, java.math.BigDecimal.valueOf(funcionario.getSalario()));
            stmt.setBoolean(4, funcionario.isStatus());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT id, nome, data_admissao, salario, status FROM funcionario ORDER BY id DESC";

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setDataAdmissao(rs.getDate("data_admissao").toLocalDate());
                f.setSalario(rs.getDouble("salario"));
                f.setStatus(rs.getBoolean("status"));
                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
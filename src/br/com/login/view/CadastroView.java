package br.com.login.view;

import br.com.login.dao.UsuarioDAO;
import br.com.login.model.Usuario;
import br.com.login.util.CriptografiaUtil;

import javax.swing.*;
import java.awt.*;

public class CadastroView extends JFrame {

    private JLabel lblTitulo;
    private JLabel lblNome;
    private JTextField txtNome;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblSenha;
    private JPasswordField txtSenha;
    private JButton btnCadastrar;
    private JButton btnVoltar;

    private UsuarioDAO usuarioDAO;

    public CadastroView() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        adicionarEventos();
    }

    private void initComponents() {
        setTitle("Cadastro de Usuário");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(null);
        painelPrincipal.setBackground(new Color(245, 246, 248));

        lblTitulo = new JLabel("Criar Nova Conta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));
        lblTitulo.setBounds(40, 25, 320, 30);
        painelPrincipal.add(lblTitulo);

        lblNome = new JLabel("Nome completo:");
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNome.setForeground(new Color(108, 117, 125));
        lblNome.setBounds(40, 75, 320, 20);
        painelPrincipal.add(lblNome);

        txtNome = new JTextField();
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNome.setBounds(40, 98, 305, 35);
        painelPrincipal.add(txtNome);

        lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(108, 117, 125));
        lblEmail.setBounds(40, 143, 320, 20);
        painelPrincipal.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(40, 166, 305, 35);
        painelPrincipal.add(txtEmail);

        lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSenha.setForeground(new Color(108, 117, 125));
        lblSenha.setBounds(40, 211, 320, 20);
        painelPrincipal.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBounds(40, 234, 305, 35);
        painelPrincipal.add(txtSenha);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setBackground(new Color(40, 167, 69));
        btnCadastrar.setForeground(new Color(33, 37, 41));
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setBounds(40, 295, 145, 38);
        painelPrincipal.add(btnCadastrar);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVoltar.setBackground(new Color(220, 224, 230));
        btnVoltar.setForeground(new Color(33, 37, 41));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBounds(200, 295, 145, 38);
        painelPrincipal.add(btnVoltar);

        add(painelPrincipal);
    }

    private void adicionarEventos() {
        btnCadastrar.addActionListener(e -> realizarCadastro());
        btnVoltar.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private void realizarCadastro() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senhaPura = new String(txtSenha.getPassword()).trim();

        if (nome.isEmpty() || email.isEmpty() || senhaPura.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos!", 
                    "Atenção", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String senhaHash = CriptografiaUtil.sha256(senhaPura);

        Usuario novoUsuario = new Usuario(nome, email, senhaHash);
        boolean sucesso = usuarioDAO.cadastrar(novoUsuario);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, 
                    "Cadastro realizado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Erro ao cadastrar. O e-mail pode já estar em uso.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
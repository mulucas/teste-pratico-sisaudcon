package br.com.login.view;

import br.com.login.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {

    private JLabel lblTitulo;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblSenha;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnLimpar;
    private JLabel lblFazerCadastro;

    private UsuarioDAO usuarioDAO;

    public LoginView() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        configurarAparencia();
        adicionarEventos();
    }

    private void initComponents() {
        setTitle("Login - Sistema");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(null);
        painelPrincipal.setBackground(new Color(245, 246, 248));

        lblTitulo = new JLabel("Bem-vindo(a)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));
        lblTitulo.setBounds(40, 30, 320, 30);
        painelPrincipal.add(lblTitulo);

        lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(108, 117, 125));
        lblEmail.setBounds(40, 80, 320, 20);
        painelPrincipal.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(40, 105, 305, 35);
        painelPrincipal.add(txtEmail);

        lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSenha.setForeground(new Color(108, 117, 125));
        lblSenha.setBounds(40, 150, 320, 20);
        painelPrincipal.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBounds(40, 175, 305, 35);
        painelPrincipal.add(txtSenha);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(13, 110, 253));
        btnEntrar.setForeground(new Color(33, 37, 41));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBounds(40, 230, 145, 38);
        painelPrincipal.add(btnEntrar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLimpar.setBackground(new Color(220, 224, 230));
        btnLimpar.setForeground(new Color(33, 37, 41));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBounds(200, 230, 145, 38);
        painelPrincipal.add(btnLimpar);

        lblFazerCadastro = new JLabel("<html><a href=''>Fazer cadastro</a></html>");
        lblFazerCadastro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFazerCadastro.setBounds(40, 290, 140, 20);
        painelPrincipal.add(lblFazerCadastro);

        add(painelPrincipal);
    }

    private void configurarAparencia() {
        lblFazerCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void adicionarEventos() {
        btnEntrar.addActionListener(e -> realizarLogin());
        btnLimpar.addActionListener(e -> limparCampos());

        lblFazerCadastro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CadastroView().setVisible(true);
            }
        });
       
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos!", 
                    "Atenção", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean autenticado = usuarioDAO.autenticar(email, senha);

        if (autenticado) {
            JOptionPane.showMessageDialog(this, 
                    "Login realizado com sucesso!", 
                    "Bem-vindo", 
                    JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            new FuncionarioView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "E-mail ou senha incorretos!", 
                    "Falha no Login", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtEmail.setText("");
        txtSenha.setText("");
        txtEmail.requestFocus();
    }
}
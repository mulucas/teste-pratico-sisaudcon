package br.com.login.view;

import br.com.login.dao.FuncionarioDAO;
import br.com.login.model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class FuncionarioView extends JFrame {

    private JTextField txtNome;
    private JFormattedTextField txtDataAdmissao;
    private JTextField txtSalario;
    private JCheckBox chkStatus;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JLabel lblVoltarLogin;
    private JTable tabelaFuncionarios;
    private DefaultTableModel modeloTabela;

    private FuncionarioDAO funcionarioDAO;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private final DecimalFormat moedaFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public FuncionarioView() {
        funcionarioDAO = new FuncionarioDAO();
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Funcionários");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(null);
        painelPrincipal.setBackground(new Color(245, 246, 248));

        JLabel lblTitulo = new JLabel("Cadastro de Funcionários");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBounds(20, 15, 300, 25);
        painelPrincipal.add(lblTitulo);

        JPanel painelForm = new JPanel(null);
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));
        painelForm.setBounds(20, 55, 300, 385);
        painelForm.setBackground(new Color(245, 246, 248));

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(15, 25, 270, 20);
        painelForm.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(15, 48, 270, 30);
        painelForm.add(txtNome);

        JLabel lblData = new JLabel("Data de Admissão (dd/MM/aaaa):");
        lblData.setBounds(15, 88, 270, 20);
        painelForm.add(lblData);

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataAdmissao = new JFormattedTextField(mascaraData);
        } catch (ParseException e) {
            e.printStackTrace();
            txtDataAdmissao = new JFormattedTextField();
        }
        txtDataAdmissao.setBounds(15, 111, 270, 30);
        txtDataAdmissao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painelForm.add(txtDataAdmissao);

        JLabel lblSalario = new JLabel("Valor do Salário (R$):");
        lblSalario.setBounds(15, 151, 270, 20);
        painelForm.add(lblSalario);

        txtSalario = new JTextField(null);
        txtSalario.setBounds(15, 174, 270, 30);
        txtSalario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSalario.setHorizontalAlignment(JTextField.RIGHT);
        
        txtSalario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // apenas números e o apagar
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Cancela o caractere se não for número
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                formatarCampoSalario();
            }
        });
        
        painelForm.add(txtSalario);

        chkStatus = new JCheckBox("Ativo");
        chkStatus.setBackground(new Color(245, 246, 248));
        chkStatus.setSelected(true);
        chkStatus.setBounds(15, 220, 270, 25);
        painelForm.add(chkStatus);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBounds(15, 275, 125, 35);
        painelForm.add(btnSalvar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(220, 224, 230));
        btnLimpar.setForeground(new Color(33, 37, 41));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBounds(160, 275, 125, 35);
        painelForm.add(btnLimpar);
        
        lblVoltarLogin = new JLabel("<html><a href=''>Voltar para o Login</a></html>", SwingConstants.CENTER);
        lblVoltarLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVoltarLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVoltarLogin.setBounds(15, 330, 270, 20);
        
        lblVoltarLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginView().setVisible(true);
            }
        });
        painelForm.add(lblVoltarLogin);

        painelPrincipal.add(painelForm);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Admissão", "Salário", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFuncionarios = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFuncionarios);
        scrollTabela.setBounds(335, 55, 385, 385);
        painelPrincipal.add(scrollTabela);

        add(painelPrincipal);

        btnSalvar.addActionListener(e -> salvarFuncionario());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    // Método auxiliar para formatar o texto do salário dinamicamente
    private void formatarCampoSalario() {
        String texto = txtSalario.getText().replaceAll("[^0-9]", ""); // Remove tudo que não for número
        if (texto.isEmpty()) {
            txtSalario.setCaretPosition(txtSalario.getText().length());
            return;
        }

        try {
            long valorLong = Long.parseLong(texto);
            // considera os dois ultimos dígitos como centavos
            double valorDouble = valorLong / 100.0;
            String formatado = moedaFormat.format(valorDouble);
            txtSalario.setText(formatado);
            // mantém o cursor no final
            txtSalario.setCaretPosition(txtSalario.getText().length());
        } catch (NumberFormatException ex) {
           
        }
    }

    private void salvarFuncionario() {
        String nome = txtNome.getText().trim();
        String dataStr = txtDataAdmissao.getText().trim();
        String salarioTextoFormatado = txtSalario.getText().trim();
        boolean status = chkStatus.isSelected();

        if (nome.isEmpty() || dataStr.contains("_") || salarioTextoFormatado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataAdmissao;
        try {
            dataAdmissao = LocalDate.parse(dataStr, dateFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/aaaa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double salario;
        try {
            // remove pontos de milhar e troca a vírgula decimal por ponto para o Java entender
            String salarioLimpo = salarioTextoFormatado.replace(".", "").replace(",", ".");
            salario = Double.parseDouble(salarioLimpo);
            if (salario < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor de salário inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Funcionario func = new Funcionario(nome, dataAdmissao, salario, status);
        boolean sucesso = funcionarioDAO.cadastrar(func);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar funcionário no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0); // Limpa as linhas atuais
        List<Funcionario> funcionarios = funcionarioDAO.listarTodos();

        for (Funcionario f : funcionarios) {
            modeloTabela.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                f.getDataAdmissao().format(dateFormatter),
                String.format("R$ %.2f", f.getSalario()),
                f.isStatus() ? "Ativo" : "Inativo"
            });
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDataAdmissao.setValue(null); 
        txtSalario.setText(null);
        chkStatus.setSelected(true);
        txtNome.requestFocus();
    }
}
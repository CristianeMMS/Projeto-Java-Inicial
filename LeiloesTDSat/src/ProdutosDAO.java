/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public int cadastrarProduto (ProdutosDTO produto){
                
        //conn = new conectaDAO().connectDB();
        conectaDAO conexao = new conectaDAO();
        if(conexao.conectar()) {
            conn = conexao.conn;
        } else {
            System.out.println("Não foi possível conectar ao banco de dados");
            return -1;
        }
        
        int status;
        try {
            prep = conn.prepareStatement("INSERT INTO produtos (nome, valor, status) VALUES (?,?,?)");
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());

            status = prep.executeUpdate();
            return status;

        } catch (SQLException ex) {
            System.out.println("Não foi possível inserir os dados!" + ex.getMessage());
            return ex.getErrorCode();

        }
        
        
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        
        return listagem;
    }
    
    public void venderProduto(int id) throws SQLException {
        
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";
         
        try {
            prep = conn.prepareStatement(sql);
            prep.setString(1, "Vendido");
            prep.setInt(2, id);

            int affectedRows = prep.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao vender o produto, nenhum registro afetado.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao vender produto: " + e.getMessage());
        } finally {
            if (prep != null) {
                prep.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}

        



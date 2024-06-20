
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

    public int cadastrarProduto(ProdutosDTO produto) {

        //conn = new conectaDAO().connectDB();
        conectaDAO conexao = new conectaDAO();
        if (conexao.conectar()) {
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

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {

        conectaDAO conexao = new conectaDAO();
        ArrayList<ProdutosDTO> listagemVendidos = new ArrayList<>();

        if (conexao.conectar()) {
            conn = conexao.conn;
        } else {
            System.out.println("Não foi possível conectar ao banco de dados");
            return listagemVendidos;
        }
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        try {
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();

            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));
                listagemVendidos.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos vendidos: " + e.getMessage());
        } finally {
            try {
                if (resultset != null) {
                    resultset.close();
                }
                if (prep != null) {
                    prep.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }

        return listagemVendidos;

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

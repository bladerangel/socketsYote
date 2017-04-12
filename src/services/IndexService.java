package services;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.CasaLayout;
import models.Tabuleiro;
import java.util.ArrayList;

public class IndexService {

    private VBox linhasTabuleiro;
    private HBox colunasTabuleiro;
    private ArrayList<CasaLayout> casas;
    private Tabuleiro tabuleiroJogo;

    public void criarTabuleiro(Pane tabuleiro) {
        tabuleiroJogo = new Tabuleiro();
        casas = new ArrayList<CasaLayout>();
        linhasTabuleiro = new VBox();
        tabuleiro.getChildren().add(linhasTabuleiro);
        for (int i = 0; i < 5; i++) {
            colunasTabuleiro = new HBox();
            tabuleiro.getChildren().add(colunasTabuleiro);
            linhasTabuleiro.getChildren().add(colunasTabuleiro);
            for (int j = 0; j < 6; j++) {
                CasaLayout casa = new CasaLayout();
                casas.add(casa);
                casa.setOnMouseClicked(event -> colocarPeca((CasaLayout) event.getSource()));
                colunasTabuleiro.getChildren().add(casa);
            }
        }
    }

    public void colocarPeca(CasaLayout casa) {
        if (casa.getCasa().getPeca().getJogador() == 0 && tabuleiroJogo.isEscolherCasa()) {
            casa.colocarPeca(tabuleiroJogo.getTurnoJogador());
            tabuleiroJogo.setEscolherCasa();
        }else if(casa.getCasa().getPeca().getJogador() != 0 && !tabuleiroJogo.isTirouPeca()) {
            tabuleiroJogo.setEscolherCasa();
            tabuleiroJogo.setTirouPeca();
            casa.removerPeca(tabuleiroJogo.getTurnoJogador());
        }
    }


    public void removerPeca(Text numeroPecas) {
        tabuleiroJogo.setTirouPeca();
        tabuleiroJogo.setPecas();
        tabuleiroJogo.setEscolherCasa();
        numeroPecas.setText(tabuleiroJogo.getPecas()+ " peças restantes");

    }

    public Tabuleiro getTabuleiroJogo() {
        return tabuleiroJogo;
    }
}

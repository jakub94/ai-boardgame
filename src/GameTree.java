import lenz.htw.gawihs.Move;

import java.util.ArrayList;

public class GameTree {


    private int alpha;
    private int beta;

    private int[][] playField;
    private int currentPlayerIndicator;


//    public int AlphaBeta(int depth, int alpha, int beta)
//    {
//        if (depth == 0)
//            return RatingFunction.evaluate(playField, currentPlayerIndicator);
//
//
//        boolean foundBestMove = false;
//
//        int best = -99999;
//
//        ArrayList<Move> possibleMoves = GameManager.getAllPossibleMoves(playField, currentPlayerIndicator);
//
////        while (ZuegeUebrig())
////        {
////            FuehreNaechstenZugAus();
////            if (PVgefunden)
////            {
////                wert = -AlphaBeta(tiefe-1, -alpha-1, -alpha);
////                if (wert > alpha && wert < beta)
////                    wert = -AlphaBeta(tiefe-1, -beta, -wert);
////            } else
////                wert = -AlphaBeta(tiefe-1, -beta, -alpha);
////            MacheZugRueckgaengig();
////            if (wert > best)
////            {
////                if (wert >= beta)
////                    return wert;
////                best = wert;
////                if (wert > alpha)
////                {
////                    alpha = wert;
////                    PVgefunden = TRUE;
////                }
////            }
////        }
////        return best;
//    }





}

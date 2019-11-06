package takred.setionpoc.tictactoe;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping(value = "/tic_tac_toe")
public class TicTacToeController {
    private boolean symbol = true;
    private List<List<String>> table = new ArrayList<>(3);

    public void fill() {
        table = new ArrayList<>(3);
        List<String> template = new ArrayList<>();
        template.add(" ");
        template.add(" ");
        template.add(" ");
        table.add(template);
        template = new ArrayList<>();
        template.add(" ");
        template.add(" ");
        template.add(" ");
        table.add(template);
        template = new ArrayList<>();
        template.add(" ");
        template.add(" ");
        template.add(" ");
        table.add(template);
        template = new ArrayList<>();
        template.add("");
        table.add(template);
    }

    @RequestMapping(value = "/play")
    public List<List<String>> newGame() {
        fill();
        return table;
    }

    @RequestMapping(value = "/play/{x}/{y}")
    public List<List<String>> game(@PathVariable("x") int x, @PathVariable("y") int y) {
        if (table.get(3).get(0).equals("Выиграли крестики.") ||
                table.get(3).get(0).equals("Выиграли нолики.") ||
                table.get(3).get(0).equals("Ничья.")) {
            return table;
        }
        table.get(3).set(0, "");
        if (x > 0 && x < 4 && y > 0 && y < 4) {
            if (checkPaste(x, y)) {
                if (symbol) {
                    table.get(x - 1).set(y - 1, "x");
                } else {
                    table.get(x - 1).set(y - 1, "o");
                }
                symbol = !symbol;
                return winner();
            } else {
                table.get(3).set(0, "Эта клетка занята. Введите другие координаты.");
                return table;
            }
        } else {
            table.get(3).set(0, "Таких координат не существует. (поле 3 на 3 клетки).");
            return table;
        }
    }

    public List<List<String>> winner() {
        List<String> symbols = new ArrayList<>();
        symbols.add("x");
        symbols.add("o");
        for (int i = 0; i < symbols.size(); i++) {
            if (table.get(0).get(0).equals(symbols.get(i)) &&
                    table.get(0).get(1).equals(symbols.get(i)) &&
                    table.get(0).get(0).equals(symbols.get(i)) ||
                    table.get(1).get(0).equals(symbols.get(i)) &&
                            table.get(1).get(1).equals(symbols.get(i)) &&
                            table.get(1).get(2).equals(symbols.get(i)) ||
                    table.get(2).get(0).equals(symbols.get(i)) &&
                            table.get(2).get(1).equals(symbols.get(i)) &&
                            table.get(2).get(2).equals(symbols.get(i)) ||
                    table.get(0).get(0).equals(symbols.get(i)) &&
                            table.get(1).get(0).equals(symbols.get(i)) &&
                            table.get(2).get(0).equals(symbols.get(i)) ||
                    table.get(0).get(1).equals(symbols.get(i)) &&
                            table.get(1).get(1).equals(symbols.get(i)) &&
                            table.get(2).get(1).equals(symbols.get(i)) ||
                    table.get(0).get(2).equals(symbols.get(i)) &&
                            table.get(1).get(2).equals(symbols.get(i)) &&
                            table.get(2).get(2).equals(symbols.get(i)) ||
                    table.get(0).get(0).equals(symbols.get(i)) &&
                            table.get(1).get(1).equals(symbols.get(i)) &&
                            table.get(2).get(2).equals(symbols.get(i)) ||
                    table.get(0).get(2).equals(symbols.get(i)) &&
                            table.get(1).get(1).equals(symbols.get(i)) &&
                            table.get(2).get(0).equals(symbols.get(i))) {
                if (symbols.get(i).equals("x")) {
                    table.get(3).set(0, "Выиграли крестики.");
                    return table;
                } else if (symbols.get(i).equals("o")) {
                    table.get(3).set(0, "Выиграли нолики.");
                    return table;
                }
            }

        }
        boolean fullness = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).equals(" ")) {
                    fullness = false;
                }
            }
        }
        if (fullness) {
            table.get(3).set(0, "Ничья.");
            return table;
        }
        return table;
    }

    public boolean checkPaste(int x, int y) {
        if (table.get(x - 1).get(y - 1).equals(" ")) {
            return true;
        }
        return false;
    }
}
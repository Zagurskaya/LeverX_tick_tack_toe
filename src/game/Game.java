package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game {
    static int[] canvas = {0, 0, 0,
            0, 0, 0,
            0, 0, 0};

    public static void main(String[] args) {
        System.out.println("Select game:");
        System.out.println("1 - with a person");
        System.out.println("2 - with a robot");
        int gameNumber = getNumber();
        if (gameNumber == 1) {
            gamePeoples();
        }
        if (gameNumber == 2) {
            gameWithRobot();
        }
    }

    static void gamePeoples() {

        boolean b;
        boolean isCurrentX = false;
        do {
            isCurrentX = !isCurrentX;
            drawCanvas();
            System.out.println("mark " + (isCurrentX ? "X" : "O"));
            int n = getNumber();
            canvas[n] = isCurrentX ? 1 : 2;
            b = !isGameOver(n);
            if (isDraw()) {
                System.out.println("Draw");
                return;
            }
        } while (b);
        drawCanvas();
        System.out.println();
        System.out.println("The winner is " + (isCurrentX ? "X" : "O") + "!");
    }

    static void gameWithRobot() {
        System.out.println("X - robot, O - person");
        System.out.println(" Who is first? 1 - robot, 2 - person");
        int first = getNumber();
        drawCanvas();
        if (first == 1) {
            gameWithRobotFirstRobot();
        }
        if (first == 2) {
            gameWithRobotFirstPerson();
        }
    }

    static void gameWithRobotFirstRobot() {
        boolean isCurrentX = true;
        int count = 1;
        int firstStep = 4;
        int n = 0;
        gameSteps(n, count, firstStep, isCurrentX);
    }

    static void gameWithRobotFirstPerson() {
        boolean isCurrentX = false;
        int count = 1;
        System.out.println("mark O");
        int n = getNumber();
        canvas[n] = 2;
        isCurrentX = !isCurrentX;
        count++;
        int firstStep = getRobotNumber(n, count, n);
        gameSteps(n, count, firstStep, isCurrentX);
    }

    static void gameSteps(int nGame, int countGame, int firstStepGame, boolean isCurrentXGame) {
        int n = nGame;
        int count = countGame;
        int firstStep = firstStepGame;
        boolean isCurrentX = isCurrentXGame;
        boolean b;
        do {
            drawCanvas();
            System.out.println("mark O");
            n = isCurrentX ? getRobotNumber(n, count, firstStep) : getNumber();
            canvas[n] = isCurrentX ? 1 : 2;
            isCurrentX = !isCurrentX;
            count++;
            b = !isGameOver(n);
            if (isDraw()) {
                System.out.println("Draw");
                return;
            }
        } while (b);
        drawCanvas();
        System.out.println();
        System.out.println("The winner is " + (!isCurrentX ? "X" : "O") + "!");
    }

    static int getNumber() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                int n = Integer.parseInt(reader.readLine());
                if (n >= 0 && n < canvas.length && canvas[n] == 0) {
                    return n;
                }
                System.out.println("Choose free cell and enter its number");
            } catch (NumberFormatException e) {
                System.out.println("Please enter the number");
            } catch (IOException e) {
            }
        }
    }

    static int getRobotNumber(int n, int count, int firstStep) {
        if (count == 1) {
            return 4;
        }
        if (count == 2) {
            if (canvas[4] == 0) {
                return 4;
            } else {
                int[] items = new int[]{0, 2, 6, 8};
                Random rand = new Random();
                return items[rand.nextInt(items.length)];
            }
        }
        if (count >= 4) {
            int win = getWinNumber();
            if (win != 999) {
                return win;
            }
            int result = getWinNumber(firstStep);
            if (result != 999) {
                return result;
            }
        }
        return (n + 1 != 4) && (n + 1 != 8) ? n + 1 : n - 1;
    }

    static int getWinNumber(int n) {
        //поиск возможностей совпадений по горизонтали
        int row = n - n % 3; //номер строки - проверяем только её
        if (canvas[row] == canvas[row + 1] && canvas[row + 2] == 0) return row + 2;
        if (canvas[row + 1] == canvas[row + 2] && canvas[row] == 0) return row;
        if (canvas[row] == canvas[row + 2] && canvas[row + 1] == 0) return row + 1;
        //поиск совпадений по вертикали
        int column = n % 3; //номер столбца - проверяем только его
        if (canvas[column] == canvas[column + 3] && canvas[column + 6] == 0) return column + 6;
        if (canvas[column + 3] == canvas[column + 6] && canvas[column] == 0) return column;
        if (canvas[column] == canvas[column + 6] && canvas[column + 3] == 0) return column + 3;
        //если значение n находится на одной из граней - возвращаем false
        //проверяем принадлежит ли к левой диагонали значение
        if (n % 4 == 0) {
            //проверяем есть ли совпадения на левой диагонали
            if (canvas[0] == canvas[4] && canvas[8] == 0) return 8;
            if (canvas[4] == canvas[8] && canvas[0] == 0) return 0;
            if (canvas[0] == canvas[8] && canvas[4] == 0) return 4;
        }
        if (n % 2 == 0) {
            //проверяем есть ли совпадения на правой диагонали
            if (canvas[2] == canvas[4] && canvas[6] == 0) return 6;
            if (canvas[4] == canvas[6] && canvas[2] == 0) return 2;
            if (canvas[2] == canvas[6] && canvas[4] == 0) return 4;
        }
        return 999;
    }

    static int getWinNumber() {
        int result = 999;
        int[] array = {0, 4, 8};
        for (int value : array) {
            int resultFor = getWinNumber(value);
            if (resultFor != 999) {
                result = resultFor;
                break;
            }
        }
        return result;
    }

    static boolean isGameOver(int n) {
        // 0 1 2
        // 3 4 5
        // 6 7 8
        //поиск совпадений по горизонтали
        int row = n - n % 3; //номер строки - проверяем только её
        if (canvas[row] == canvas[row + 1] &&
                canvas[row] == canvas[row + 2]) return true;
        //поиск совпадений по вертикали
        int column = n % 3; //номер столбца - проверяем только его
        if (canvas[column] == canvas[column + 3])
            if (canvas[column] == canvas[column + 6]) return true;
        //мы здесь, значит, первый поиск не положительного результата
        //если значение n находится на одной из граней - возвращаем false
        if (n % 2 != 0) return false;
        //проверяем принадлежит ли к левой диагонали значение
        if (n % 4 == 0) {
            //проверяем есть ли совпадения на левой диагонали
            if (canvas[0] == canvas[4] &&
                    canvas[0] == canvas[8]) return true;
            if (n != 4) return false;
        }
        return canvas[2] == canvas[4] &&
                canvas[2] == canvas[6];
    }

    static void drawCanvas() {
        System.out.println("     |     |     ");
        for (int i = 0; i < canvas.length; i++) {
            if (i != 0) {
                if (i % 3 == 0) {
                    System.out.println();
                    System.out.println("_____|_____|_____");
                    System.out.println("     |     |     ");
                } else
                    System.out.print("|");
            }

            if (canvas[i] == 0) System.out.print("  " + i + "  ");
            if (canvas[i] == 1) System.out.print("  X  ");
            if (canvas[i] == 2) System.out.print("  O  ");
        }
        System.out.println();
        System.out.println("     |     |     ");
    }

    public static boolean isDraw() {
        for (int n : canvas) {
            if (n == 0) return false;
        }
        return true;
    }
}
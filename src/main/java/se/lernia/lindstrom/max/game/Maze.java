package se.lernia.lindstrom.max.game;

import se.lernia.lindstrom.max.entities.Monster;
import se.lernia.lindstrom.max.entities.Player;
import se.lernia.lindstrom.max.entities.Position;
import se.lernia.lindstrom.max.items.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Maze {
    private final int[][] maze = {
            {1, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 1},
            {0, 1, 1, 1, 0, 0},
            {1, 1, 0, 1, 0, 0},
            {1, 0, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 0}
    };

    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Monster> monsters = new ArrayList<>();

    public int[][] getMaze() {
        return maze;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    private boolean isValidPosition(Position pos) {
        return pos.x() >= 0 && pos.x() < maze.length && pos.y() >= 0 && pos.y() < maze[0].length;
    }

    private boolean canMoveTo(Position pos) {
        return maze[pos.x()][pos.y()] != 0;
    }

    public Monster checkForMonster(Position position) {
        return monsters.stream()
                .filter(monster -> monster.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    public void checkForItem(Player player) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getPosition().equals(player.getPosition())) {
                player.addItem(item, false);
                iterator.remove();
            }
        }
    }

    public ArrayList<Position> getValidMoves(Position currentPos) {
        return getNeighbours(currentPos)
                .stream()
                .filter(position -> isValidPosition(position) && canMoveTo(position))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Position> getNeighbours(Position pos) {
        ArrayList<Position> neighbours = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i == 0 && j == 0) || (i != 0 && j != 0)) {
                    continue;
                }
                Position newPos = Position.of(pos.x() + i, pos.y() + j);
                neighbours.add(newPos);
            }
        }
        return neighbours;
    }

    public String getMoveDirection(Position oldPos, Position newPos) {
        if (oldPos.x() + 1 == newPos.x() && oldPos.y() == newPos.y()) {
            return "South";
        } else if (oldPos.x() - 1 == newPos.x() && oldPos.y() == newPos.y()) {
            return "North";
        } else if (oldPos.x() == newPos.x() && oldPos.y() - 1 == newPos.y()) {
            return "West";
        } else {
            return "East";
        }
    }

    public void updateMaze() {
        if (monsters.size() < 4) {
            Randomize.addRandomMonsters(this, 1);
        }
        if (items.size() < 4) {
            Randomize.addRandomLoot(this, 1);
        }
    }
}

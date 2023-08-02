package fundamentals;

import java.util.LinkedList;

public class GameMath
{
    public static boolean isCoordsEqual(Coordinates primary, Coordinates secondary)
    {
        return primary.getX() == secondary.getX() && primary.getY() == secondary.getY();
    }

    public static boolean isOpposingCoordDirections(Coordinates primary, Coordinates secondary)
    {
        return Math.abs(primary.getDegrees() - secondary.getDegrees()) == 180; 
    }    

    public static boolean probability(double prob_pct)
    {
        prob_pct = Math.max(Math.min(prob_pct, 1.0), 0.0);
        return prob_pct == 1.0 || Math.random() < prob_pct;
    }

    static private void remergeMinMaxEnemyRoutes(LinkedList<LinkedList<Coordinates>> routes, 
	LinkedList<LinkedList<Coordinates>> left_routes, LinkedList<LinkedList<Coordinates>> right_routes) {
		int left_index = 0;
		int right_index = 0;
		for(int i = 0; i < left_routes.size() + right_routes.size(); i++) {
			if(left_index < left_routes.size() && (right_index >= right_routes.size() 
            || left_routes.get(left_index).size() < right_routes.get(right_index).size())) {
				routes.set(i, left_routes.get(left_index));
				left_index++;
			}
			else {
				routes.set(i, right_routes.get(right_index));
				right_index++;
			}
		}
	}

	static public void minMaxLengthEnemyRoutesMergeSort(LinkedList<LinkedList<Coordinates>> routes) {
		if(routes.size() == 1) {
			return;
		}

		final int left_routes_size = routes.size() / 2;
		final int right_routes_size = routes.size() - (routes.size() / 2);
		LinkedList<LinkedList<Coordinates>> left_routes = new LinkedList<LinkedList<Coordinates>>();
		LinkedList<LinkedList<Coordinates>> right_routes = new LinkedList<LinkedList<Coordinates>>();

		for(int i = 0; i < left_routes_size; i++) {
			left_routes.addLast(routes.get(i));
		}

		for(int i = 0; i < right_routes_size; i++) {
			right_routes.addLast(routes.get(left_routes.size() + i));
		}

		minMaxLengthEnemyRoutesMergeSort(left_routes);
		minMaxLengthEnemyRoutesMergeSort(right_routes);
		remergeMinMaxEnemyRoutes(routes, left_routes, right_routes);
	}
}

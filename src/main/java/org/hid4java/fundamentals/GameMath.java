package org.hid4java.fundamentals;

import java.util.BitSet;
import java.util.LinkedList;

public class GameMath
{
	public static BitSet getBinary(int register, int size) {
        BitSet binary = new BitSet(size);
        register = Math.abs(register);
        for(int i = size - 1; i >= 0 && register > 0; i--) {
            int value = (int)Math.pow(2, i);
            if(value <= register) {
                register -= value;
                binary.set(size - i - 1);
            }
        }

        return binary;
    }

	public static double getDistance(double x0, double y0, double x1, double y1)
	{
		return Math.pow(Math.pow(x1 - x0, 2.0) + Math.pow(y1 - y0, 2.0), 0.5);
	}

    public static boolean isCoordsEqual(Coordinates primary, Coordinates secondary)
    {
        return primary != null && secondary != null 
		&& primary.getX() == secondary.getX() && primary.getY() == secondary.getY();
    }

    public static boolean isOpposingCoordDirections(Coordinates primary, Coordinates secondary)
    {
        return primary != null && secondary != null
		&& Math.abs(primary.getDegrees() - secondary.getDegrees()) == 180; 
    }    

    public static boolean probability(double prob_pct)
    {
        prob_pct = Math.max(Math.min(prob_pct, 1.0), 0.0);  
        return prob_pct == 1.0 || Math.random() < prob_pct;
    }

	public static Coordinates getRadialDisplacement(int radius_units, int degrees)
	{
		double radians = (double)degrees * (Math.PI / 180.0);
		double delta_stage_x = Math.pow(Math.pow(radius_units, 2) - Math.pow((double)radius_units * Math.sin(radians), 2), 0.5);
		double delta_stage_y = Math.pow(Math.pow(radius_units, 2) - Math.pow((double)radius_units * Math.cos(radians), 2), 0.5);
		delta_stage_x = (degrees <= 90 || degrees > 270) ? delta_stage_x : -delta_stage_x;
		delta_stage_y = (degrees <= 180) ? delta_stage_y : -delta_stage_y;

		return new Coordinates((int)delta_stage_x, (int)delta_stage_y, 0);
	}

	public static boolean coordsContainsCoord(LinkedList<Coordinates> coords, Coordinates coord)
	{
		for(int current_coord_index = 0; coords != null && current_coord_index < coords.size(); current_coord_index++)
		{
			if(isCoordsEqual(coords.get(current_coord_index), coord))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean routeContainsCoords(LinkedList<Coordinates> route, LinkedList<Coordinates> coords)
	{
		for(int current_route_index = 0; route != null && current_route_index < route.size(); current_route_index++)
		{
			if(coordsContainsCoord(coords, route.get(current_route_index)))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isCoordInStageTunnel(Coordinates coord)
	{
		try
		{
			Coordinates[][] stage_tunnel_regions = Constants.STAGE_CHARACTERISTICS.STAGE_TUNNEL_REGIONS;
			for(int current_tunnel_index = 0; current_tunnel_index < stage_tunnel_regions.length; current_tunnel_index++)
			{
				Coordinates stage_coord_0 = stage_tunnel_regions[current_tunnel_index][0];
				Coordinates stage_coord_1 = stage_tunnel_regions[current_tunnel_index][1];

				int min_x = Math.min(stage_coord_0.getX(), stage_coord_1.getX());
				int max_x = Math.max(stage_coord_0.getX(), stage_coord_1.getX());
				int min_y = Math.min(stage_coord_0.getY(), stage_coord_1.getY());
				int max_y = Math.max(stage_coord_0.getY(), stage_coord_1.getY());

				if(coord.getX() >= min_x && coord.getX() <= max_x && coord.getY() >= min_y && coord.getY() <= max_y)
				{
					return true;
				}
			}
		}
		catch(IndexOutOfBoundsException e) {}
		return false; 
	}

    private static void remergeMinMaxEnemyRoutes(LinkedList<LinkedList<Coordinates>> routes, 
	LinkedList<LinkedList<Coordinates>> left_routes, LinkedList<LinkedList<Coordinates>> right_routes) 
	{
		int left_index = 0;
		int right_index = 0;
		for(int i = 0; i < left_routes.size() + right_routes.size(); i++) 
		{
			if(left_index < left_routes.size() && (right_index >= right_routes.size() 
            || left_routes.get(left_index).size() < right_routes.get(right_index).size())) 
			{
				routes.set(i, left_routes.get(left_index));
				left_index++;
			}
			else 
			{
				routes.set(i, right_routes.get(right_index));
				right_index++;
			}
		}
	}

	public static void minMaxLengthEnemyRoutesMergeSort(LinkedList<LinkedList<Coordinates>> routes) 
	{
		if(routes.size() == 1) 
		{
			return;
		}

		final int left_routes_size = routes.size() / 2;
		final int right_routes_size = routes.size() - (routes.size() / 2);
		LinkedList<LinkedList<Coordinates>> left_routes = new LinkedList<LinkedList<Coordinates>>();
		LinkedList<LinkedList<Coordinates>> right_routes = new LinkedList<LinkedList<Coordinates>>();

		for(int i = 0; i < left_routes_size; i++) 
		{
			left_routes.addLast(routes.get(i));
		}

		for(int i = 0; i < right_routes_size; i++) 
		{
			right_routes.addLast(routes.get(left_routes.size() + i));
		}

		minMaxLengthEnemyRoutesMergeSort(left_routes);
		minMaxLengthEnemyRoutesMergeSort(right_routes);
		remergeMinMaxEnemyRoutes(routes, left_routes, right_routes);
	}
}

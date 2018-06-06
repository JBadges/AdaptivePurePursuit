package control;

import java.util.ArrayList;
import java.util.List;

import control.exception.PathCreationError;
import util.Point2;

public class Path {

	private List<PathSegment> segments;

	/**
	 * Creates the path segment by pairing waypoints, {0,1},{1,2}...
	 * 
	 * @param waypoints
	 */
	public Path(List<Point2> waypoints) {
		if (waypoints.size() < 2) {
			throw new PathCreationError("Two or more waypoints needed to make a path from a waypoint list");
		}
		segments = new ArrayList<>();

		//Create a line between all the points
		for (int i = 0; i < waypoints.size() - 1; i++) {
			segments.add(new PathSegment(waypoints.get(i), waypoints.get(i + 1)));
		}
	}

	/**
	 * Creates the path segment by pairing waypoints, {0,1},{1,2}...
	 * 
	 * @param waypoints
	 */
	public Path(Point2... waypoints) {
		if (waypoints.length < 2) {
			throw new PathCreationError("Two or more waypoints needed to make a path from a waypoint array");
		}
		segments = new ArrayList<>();

		//Create a line between all the points
		for (int i = 0; i < waypoints.length - 1; i++) {
			segments.add(new PathSegment(waypoints[i], waypoints[i + 1]));
		}
	}

	public List<PathSegment> getSegments() {
		return segments;
	}
}
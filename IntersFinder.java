package main;

import java.util.ArrayList;
import java.util.List;

public class IntersFinder
{
	static final double α = 0.85;        // angle α
	static final double[] x0 = {0, 0};   // starting vector x0
	static final double[] u1 = {1, 0};   // } - basis vectors u1, u2 
	static final double[] u2 = {0, 1};   // }
	
	// class for the point x_t(rho), saving x and y coordinates
	static record Point(int t, double rho, double x, double y) {
		Point(int t, int k) {
			this(t, (double)k / (double)t,
					Math.cos(t * α) * (x0[0] + k*u1[0] + (t-k)*u2[0]) - Math.sin(t * α) * (x0[1] + k*u1[1] + (t-k)*u2[1]),
					Math.sin(t * α) * (x0[0] + k*u1[0] + (t-k)*u2[0]) + Math.cos(t * α) * (x0[1] + k*u1[1] + (t-k)*u2[1])
				);
		}
		
		double dist(Point q) {
			return Math.hypot(x - q.x, y - q.y);
		}
	}
	
	public static void main(String[] args) {
		// list of points found so far
		List<Point> points = new ArrayList<>();
		
		double min_dist = Double.MAX_VALUE;
		
		for(int t = 0; t < 3000; t++) {
      // just give a little notice every 50th iteration, to show progress
			if(t % 50 == 0) {
				System.out.println(t + "...");
			}
			List<Point> nlist = new ArrayList<>();
			
			// go through every point x_t(m/t) on the line x_t(.)
			for(int m = 0; m <= t; m++) {
				Point p = new Point(t, m);
				for(Point q: points) {
					double dist = p.dist(q);
					
					// every time we find a pair of points with smaller distance than any previous pair, we print them
					if(dist < min_dist) {
						System.out.println("New minimum distance: " + dist);
						System.out.println("  x_" + q.t + "(" + q.rho + ")  ->  " + q.x + " " + q.y);
						System.out.println("  x_" + t + "(" + p.rho + ")  ->  " + p.x + " " + p.y);
						min_dist = dist;
					}
				}
				nlist.add(p);
			}
			points.addAll(nlist);
		}
	}
}

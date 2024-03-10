// Mustafa Tufan 221401029 BİL 211 Homework 2

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, MouseListener {

	ArrayList<Object> all = new ArrayList<>();
	AirCraft ac;

	public Game() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLayout(new GridLayout(1, 1));
		addKeyListener(this);
		addMouseListener(this);
		setVisible(true);
	}
	
	// Paints depending on the type of entity
	
	public void paint(Graphics g) {
		super.paint(g);
		checkEveryone();
		for (int i = 0; i < all.size(); i++) {
			Object object = all.get(i);
			if (object instanceof AirCraft.Bullet) {
				AirCraft.Bullet entity = (AirCraft.Bullet) object;
				if (entity.exists) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			} else if (object instanceof Friend.FriendBullet) {
				Friend.FriendBullet entity = (Friend.FriendBullet) object;
				if (entity.exists) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			} else if (object instanceof Enemy.EnemyBullet) {
				Enemy.EnemyBullet entity = (Enemy.EnemyBullet) object;
				if (entity.exists) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			} else if (object instanceof AirCraft) {
				AirCraft entity = (AirCraft) object;
				if (entity.alive) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			} else if (object instanceof Friend) {
				Friend entity = (Friend) object;
				if (entity.alive) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			} else if (object instanceof Enemy) {
				Enemy entity = (Enemy) object;
				if (entity.alive) {
					g.setColor(entity.color);
					g.fillRect(entity.x, entity.y, entity.sideLength, entity.sideLength);
				}
			}
		}
	}

	// Checks if the game is ended already
	// Calls new window if it ended depending on the way it ended

	private void checkEveryone() {
		int aliveEnemies = 0;
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i) instanceof AirCraft) {
				AirCraft entity = (AirCraft) all.get(i);
				if (!entity.alive) {
					dispose();
					new EndWindow(false);
					break;
				}
			} else if (all.get(i) instanceof Enemy) {
				Enemy entity = (Enemy) all.get(i);
				if (entity.alive)
					aliveEnemies++;
			}
		}

		if (aliveEnemies == 0) {
			dispose();
			new EndWindow(true);
		}
	}

	// Adds friends and enemies in random places

	private void addRandomly(Object object) {
		Random generator = new Random();
		while (true) {
			int x = (generator.nextInt(48) + 1) * 10;
			int y = (generator.nextInt(46) + 3) * 10;
			if (checkLocation(x, y)) {
				if (object instanceof Friend) {
					Friend entity = (Friend) object;
					entity.x = x;
					entity.y = y;
					all.add(entity);
					return;
				} else if (object instanceof Enemy) {
					Enemy entity = (Enemy) object;
					entity.x = x;
					entity.y = y;
					all.add(entity);
					return;
				}
			}
		}
	}

	// checks if potential position of entity is empty

	private boolean checkLocation(int nx, int ny) {
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i) instanceof AirCraft) {
				AirCraft entity = (AirCraft) all.get(i);
				if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1) && ny >= entity.y
						&& ny <= (entity.y + entity.sideLength - 1)) {
					return false;
				}
			} else if (all.get(i) instanceof Friend) {
				Friend entity = (Friend) all.get(i);
				if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1) && ny >= entity.y
						&& ny <= (entity.y + entity.sideLength - 1)) {
					return false;
				}
			} else if (all.get(i) instanceof Enemy) {
				Enemy entity = (Enemy) all.get(i);
				if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1) && ny >= entity.y
						&& ny <= (entity.y + entity.sideLength - 1)) {
					return false;
				}
			}
		}
		return true;
	}

	// Randomly moves entities

	private void moveRandomly(int id) {
		while (true) {
			Random generator = new Random();
			int way = generator.nextInt(4);
			int dx = 0;
			int dy = 0;
			switch (way) {
			case 0:
				dx = 0;
				dy = -10;
				break;
			case 1:
				dx = -10;
				dy = 0;
				break;
			case 2:
				dx = 0;
				dy = 10;
				break;
			case 3:
				dx = 10;
				dy = 0;
				break;
			}

			Object object = all.get(id);

			if (object instanceof Friend) {
				Friend entity = (Friend) object;
				if (checkLocation(entity.x + dx, entity.y + dy)
						&& inBorders(entity.x + dx, entity.y + dy)) {
					entity.x += dx;
					entity.y += dy;
					break;
				}
			}

			else if (object instanceof Enemy) {
				Enemy entity = (Enemy) object;
				if (checkLocation(entity.x + dx, entity.y + dy)
						&& inBorders(entity.x + dx, entity.y + dy)) {
					entity.x += dx;
					entity.y += dy;
					break;
				}
			}
		}
		repaint();
	}

	// Checks if destination is in borders

	private boolean inBorders(int nx, int ny) {
		if (nx >= 10 && nx <= 489 && ny >= 30 && ny <= 489)
			return true;
		return false;
	}

	// Checks if AirCraft is inside someone
	// Game ends with lose if it is inside an enemy ship
	
	private void checkInside() {
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i) instanceof Enemy) {
				Enemy entity = (Enemy) all.get(i);
				if (ac.x >= entity.x && ac.x <= (entity.x + entity.sideLength - 1) && ac.y >= entity.y
						&& ac.y <= (entity.y + entity.sideLength - 1)) {
					dispose();
					new EndWindow(false);
				}
			}
		}
	}

	// New window for end screen
	
	public class EndWindow extends JFrame {

		public EndWindow(boolean win) {
			setSize(300, 100);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

			if (win) {
				JLabel label = new JLabel("Oyunu kazandınız");
				this.add(label);
			}

			else {
				JLabel label = new JLabel("Oyunu kaybettiniz");
				this.add(label);
			}
		}
	}
	
	public class AirCraft extends Thread {

		int x = 250;
		int y = 250;
		final int sideLength = 10;
		boolean alive = true;
		Color color = Color.red;

		public AirCraft() {
			ac = this;
			all.add(this);
		}

		@Override
		public void run() {
		}

		public class Bullet extends Thread {
			int x;
			int y;
			Color color = Color.orange;
			final int sideLength = 5;
			String way;
			boolean exists = true;
			
			public Bullet(String way) {
				this.way = way;

				if (way.equals("right")) {
					this.x = ac.x + 21;
					this.y = ac.y + 2;
				} else if (way.equals("left")) {
					this.x = ac.x - 11;
					this.y = ac.y + 2;
				}
			}

			// Checks if bullet hit somebody
			
			private boolean hit(int nx, int ny) {
				for (int i = 0; i < all.size(); i++) {
					if (all.get(i) instanceof AirCraft) {
						AirCraft entity = (AirCraft) all.get(i);
						if (entity.alive && nx >= entity.x
								&& nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							return true;
						}
					} else if (all.get(i) instanceof Friend) {
						Friend entity = (Friend) all.get(i);
						if (entity.alive && nx >= entity.x
								&& nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							return true;
						}
					} else if (all.get(i) instanceof Enemy) {
						Enemy entity = (Enemy) all.get(i);
						if (entity.alive && nx >= entity.x
								&& nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							entity.alive = false;
							entity.x = 0;
							entity.y = 0;
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public void run() {
				while (exists) {
					if (way.equals("right")) {
						if (inBorders(x + 10, y) && !hit(x + 10, y))
							x += 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}
					} else {
						if (inBorders(x - 10, y) && !hit(x - 10, y))
							x -= 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}
					}

					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}
		}
	}

	public class Friend extends Thread {

		int x;
		int y;
		final int sideLength = 10;
		boolean alive = true;
		int id;
		int counter = 0;
		Color color = Color.green;

		public Friend() {
			id = all.size();
			addRandomly(this);
		}

		@Override
		public void run() {
			while (this.alive) {
				moveRandomly(id);
				try {
					counter++;
					sleep(500);
					if (counter % 2 == 0) {
						FriendBullet rightBullet = new FriendBullet("right", this.id);
						FriendBullet leftBullet = new FriendBullet("left", this.id);
						all.add(rightBullet);
						all.add(leftBullet);
						rightBullet.start();
						leftBullet.start();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}

		public class FriendBullet extends Thread {
			int x;
			int y;
			Color color = new Color(112, 48, 160);
			final int sideLength = 5;
			String way;
			boolean exists = true;

			public FriendBullet(String way, int id) {
				if (all.get(id) instanceof Friend) {
					Friend entity = (Friend) all.get(id);
					this.way = way;

					if (way.equals("right")) {
						this.x = entity.x + 21;
						this.y = entity.y + 2;
					} else if (way.equals("left")) {
						this.x = entity.x - 11;
						this.y = entity.y + 2;
					}
				}
			}

			// Checks if bullet hit somebody
			
			private boolean hit(int nx, int ny) {
				for (int i = 0; i < all.size(); i++) {
					if (all.get(i) instanceof AirCraft) {
						AirCraft entity = (AirCraft) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							return true;
						}
					} else if (all.get(i) instanceof Friend) {
						Friend entity = (Friend) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							return true;
						}
					} else if (all.get(i) instanceof Enemy) {
						Enemy entity = (Enemy) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							entity.alive = false;
							entity.x = 0;
							entity.y = 0;
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public void run() {
				while (exists) {
					if (way.equals("right")) {
						if (inBorders(x + 10, y) && !hit(x, y))
							x += 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}

					} else if (way.equals("left")) {
						if (inBorders(x - 10, y) && !hit(x, y))
							x -= 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}
					}

					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}
		}
	}

	public class Enemy extends Thread {

		int x;
		int y;
		final int sideLength = 10;
		boolean alive = true;
		int id;
		int counter = 0;
		Color color = Color.black;

		public Enemy() {
			id = all.size();
			addRandomly(this);
		}

		@Override
		public void run() {
			while (this.alive) {
				moveRandomly(id);
				try {
					counter++;
					sleep(500);
					if (counter % 2 == 0) {
						EnemyBullet rightBullet = new EnemyBullet("right", this.id);
						EnemyBullet leftBullet = new EnemyBullet("left", this.id);
						all.add(rightBullet);
						all.add(leftBullet);
						rightBullet.start();
						leftBullet.start();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}

		public class EnemyBullet extends Thread {
			int x;
			int y;
			Color color = Color.blue;
			final int sideLength = 5;
			String way;
			boolean exists = true;

			public EnemyBullet(String way, int id) {
				if (all.get(id) instanceof Enemy) {
					Enemy entity = (Enemy) all.get(id);
					this.way = way;

					if (way.equals("right")) {
						this.x = entity.x + 21;
						this.y = entity.y + 2;
					} else if (way.equals("left")) {
						this.x = entity.x - 11;
						this.y = entity.y + 2;
					}
				}
			}

			// Checks if bullet hit somebody
			
			private boolean hit(int nx, int ny) {
				for (int i = 0; i < all.size(); i++) {
					if (all.get(i) instanceof AirCraft) {
						AirCraft entity = (AirCraft) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							entity.alive = false;
							entity.x = 0;
							entity.y = 0;
							return true;
						}
					} else if (all.get(i) instanceof Friend) {
						Friend entity = (Friend) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							entity.alive = false;
							entity.x = 0;
							entity.y = 0;
							return true;
						}
					} else if (all.get(i) instanceof Enemy) {
						Enemy entity = (Enemy) all.get(i);
						if (nx >= entity.x && nx <= (entity.x + entity.sideLength - 1)
								&& ny >= entity.y
								&& ny <= (entity.y + entity.sideLength - 1)) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public void run() {
				while (exists) {
					if (way.equals("right")) {
						if (inBorders(x + 10, y) && !hit(x, y))
							x += 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}
					} else if (way.equals("left")) {
						if (inBorders(x - 10, y) && !hit(x, y))
							x -= 10;
						else {
							x = 0;
							y = 0;
							exists = false;
							return;
						}
					}

					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			if (inBorders(ac.x, ac.y - 10))
				ac.y = ac.y - 10;
			checkInside();
			break;
		case 'a':
			if (inBorders(ac.x - 10, ac.y))
				ac.x = ac.x - 10;
			checkInside();
			break;
		case 's':
			if (inBorders(ac.x, ac.y + 10))
				ac.y = ac.y + 10;
			checkInside();
			break;
		case 'd':
			if (inBorders(ac.x + 10, ac.y))
				ac.x = ac.x + 10;
			checkInside();
			break;
		case 'W':
			if (inBorders(ac.x, ac.y - 10))
				ac.y = ac.y - 10;
			checkInside();
			break;
		case 'A':
			if (inBorders(ac.x - 10, ac.y))
				ac.x = ac.x - 10;
			checkInside();
			break;
		case 'S':
			if (inBorders(ac.x, ac.y + 10))
				ac.y = ac.y + 10;
			checkInside();
			break;
		case 'D':
			if (inBorders(ac.x + 10, ac.y))
				ac.x = ac.x + 10;
			checkInside();
			break;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (ac.alive) {
			AirCraft.Bullet rightBullet = ac.new Bullet("right");
			AirCraft.Bullet leftBullet = ac.new Bullet("left");
			all.add(rightBullet);
			all.add(leftBullet);
			rightBullet.start();
			leftBullet.start();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}

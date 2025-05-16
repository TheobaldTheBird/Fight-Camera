# Fight Camera

**Gives a fighting game style, side on view of 2 players. Fully compatible with Flashback replays**

![](https://github.com/TheobaldTheBird/Fight-Camera/blob/a3cce29a1898a8b7c0575a38b6bceac3c5c4b889/fightcam.gif)

# Usage:

/fightcam players <player1> <player2> to set the camera's target players

/fightcam toggle to toggle fightcam (bound to ; by default)

/fightcam smooth <factor> to set the lerp factor for the camera. Values closer to 0 give smoother camera movement, values closer to 1 give more reactive camera movment. Defaults to 0.6
### Camera distance

/fightcam distance <distance> set the camera distance to the anchor position in blocks (changes camera mode to static)

/fightcam distance auto makes it so the camera adjusts the distance automatically to keep the players in view (base distance is adjustable with forward and backward keys)
### Camera Height

/fightcam height <height> sets the camera's height relative to the anchor position in blocks

/fightcam height avg sets the camera to follow the average height of the players (good for viewing mace and crystal fights with constant height changes)

/fightcam height ground sets the camera to follow the average height of the last grounded height of the players (good for gamemodes with little height change/flat terrain)
### Camera Anchor

/fightcam anchor avg sets the camera to follow the average position of both players

/fightcam anchor <p1/p2> sets the camera to follow the first or second player (automatically sets distance to be fixed)

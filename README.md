Team Members
Qian Li (liqian12), Jing Jing (jingj93), Liyi Wang (liyiw)

Implemented Features
1.	Editor Mode:
•	Create/edit/delete/save a game: When entering the editor mode, a list of the game names is shown. A singleton class was used to store an arraylist of Game objects. When clicking “add” button, a new Game object and an editor version file to store this game are created. The object is added to the game list. To edit the game, the file is read to get all the information of the game. When clicking “save” button, the file is rewritten. When deleting the game, the object is removed from the arraylist and related files are also deleted.
•	Create/edit/delete a page: After choosing a game to edit, there shows a list of the pages this game contains. Each game object contains an arraylist of Page objects. Editors can click “add”, “edit” and “delete” button to change the objects in the Page arraylist.
•	Edit details of shapes for a single page: An editor can add shapes into the current page. The shapes can be created by clicking-and-dragging from the lower part (the same position as the possession list in the player mode). Shapes that are put beneath the lower part can not be created, and shapes put on the boundary of upper and lower parts will be relocated to the upper part, just close to the boundary line. The editor can edit details of the shapes, including:
o	Drag around the screen to reset the location of the shape
o	Change the shape name
o	Change the visibility of the shape
o	Edit the scripts associated to the current shape, i.e. adding/deleting onClick, onEnter, onDrop scripts by simply choosing items from spinners
o	Delete the shape
o	Copy/cut/paste the shape in the same game (can be in the different pages)
For the shape with an image specifically, the editor can
o	Change the movability of the shape
o	Change the width and height of the shape
For the shape with text specifically, the editor can
o	Edit the text content
o	Change the font size and show the change lively
2.	Player Mode:
•	Choosing a game to play: When entering the player mode, a singleton class is created. Game objects are internally created with the information inside of corresponding files and be stored in the current singleton. All the games are shown in a list and users can choose any game they want to play with a click. 
•	Automatically save game progress: Every time when users exit the game they’re playing now, our app will help users save the game progress and they can resume it later. We implement this function through generating and updating corresponding files internally automatically. If the game is played for the first time and exit, a new file will be created and store the information. If not the first time, we simply rewrite the file created before. These files are named as the  player version files of each game.
•	Restart/continue: When choosing a game, user will be asked to choose whether to restart or to continue with the progress saved by last exit (notice: if the game has never been played before, user won’t get this prompt). Restart means that we will create the Game objects directly with information from the editor version files. Otherwise continue will create the Game with the player version file information.

Extension
1.	Editor Features
•	Error checking
o	Alert if the editor are trying to add games/pages/shapes with an invalid name (such as an existed name)
o	Alert if the editor are trying to delete the page1
o	Alert if the editor are trying to add an empty shape name, width or height
o	Alert if the editor are trying to add invalid scripts, including
♣	Should not goto the current page itself
♣	Should not add existed scripts
•	Slide to view all the shapes. We have implemented horizontalScrollView in the lower part of the screen, which contains all the shapes for the editor to choose to add in the current page. If the touch movement is roughly horizontal, the editor can slide the lower part left/right to view all the shapes. If the touch movement is roughly vertical, the editor can create and drag a new shape to put on the upper part. This is potentially good to support more kinds of shapes.
•	Rich text support. The editor can change font size and show the changes lively while they are dragging the seekbar. The specific font size that they have chosen will then be shown in a toast. 
•	Copy/cut/paste shape. Shapes are allowed to be duplicated and moved from one page to another through the same game.
•	Transition between Activity. Add fade-in and fade-out transition effect each time the activity changes. This feature is also applied to player mode.
2.  Player Features
•	Slide to view all the shapes. The player can slide the possession list to view all the possessions he/she has. Same as the editor view.
•	Shrink into possessions. Shapes can automatically shrink and position themselves neatly when dragged to the possessions area.

Reference
1.	AlertDialog, http://blog.csdn.net/liang5630/article/details/44098899.


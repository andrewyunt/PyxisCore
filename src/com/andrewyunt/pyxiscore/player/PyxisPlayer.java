package com.andrewyunt.pyxiscore.player;

import com.andrewyunt.pyxiscore.menus.PathsMenuGUI;

public class PyxisPlayer {

    private PathsMenuGUI pathsMenuInstance;

    public PathsMenuGUI getPathsMenu() {

        return pathsMenuInstance;
    }

    public void setPathsMenu(PathsMenuGUI pathsMenuInstance) {

        this.pathsMenuInstance = pathsMenuInstance;
    }
}
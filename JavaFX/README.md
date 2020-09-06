# Arcade
Arcade is a CSCI1302 (Introduction to Programming in Java II) final project that was developed with the help of Hazim Mohamed. The project consists of a compilation of two very popular retro games: Tetris and Minesweeper. Keeping true to the game, both were developed to nearly 100% authenticity when considering funcationality. Minesweeper remains true to its origins both in design and functionality.

Clone this repository to test it out for yourselves! Below are some further instructions.

## Prerequisites
- Java SDK 14
- JavaFX 14 (https://gluonhq.com/products/javafx/) 

## Running the Jar
The jar file cannot be opened directly. You have to load it with JavaFX.

```shell script
java --module-path %path-to-javafx-lib% --add-modules javafx.controls,javafx.fxml,javafx.media -jar Arcade.jar 
```

replace `%path-to-javafx-lib%` with the actual path to the javafx library.

For example, I would run using:
```shell script
java --module-path /Users/shawnholman/Desktop/javafx-sdk-14.0.2.1/lib --add-modules javafx.controls,javafx.fxml,javafx.media -jar Arcade.jar 
```


## Building
You can build this project by opening it in IntelliJ and using the "Build" configuration already included.

## Authors

* **Shawn Holman** - *Frontend and Backend* - [The_iKick](https://github.com/The-iKick)
* **Hazim Mohamed** - *Backend* [HazimMohamed] (https://github.com/HazimMohamed)

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgement

See [ATTRIBUTIONS.md](https://github.com/The-iKick/Projects/blob/master/Arcade/ATTRIBUTIONS.md)
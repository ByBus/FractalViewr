# Fractal Viewr

---
Desktop app based on compose for desktop framework and coroutines.
The application allows you to view several types of fractals in real time, zoom and scroll through them.
Also, possible to create gradients with gradient generator tool and apply them - for different colorization of fractal.
Now available these fractals: Mandelbrot, set of Julia, Burning Ship, Phoenix, set of Newton Basins.

# Ui
![](images/ui.jpg?raw=true)
## Viewport
The main working area of the app. To interact with you can use mouse left button to drag current visible area or zoom in and out with mouse wheel.

## Toolbar
- ***Undo button*** - allows to undo previous drag or scroll action
- ***Reset button*** - shows initial view of the fractal
- ***Save button*** - opens save image dialog which also allows to recalculate image with bigger resolution before saving
- ***Create button*** - opens gradient maker dialog. User-created gradients will appear at the top of the gradient list.

> Save image dialog
![Save image dialog](images/resize_save_dialog.jpg?raw=true)

> Gradient maker
![Gradient maker](images/gradient_maker.jpg?raw=true)

## Selecting fractal
To choose fractal to draw in viewport choose one from dropdown menu in sidebar titled as *"Fractal"*.
Some fractals, such as *Julia* have additional variations. In this case, new dropdown menu appears under first one.

## Colorization
Fractals can be represented in different colors, it's helpful for revealing details. To apply new colors click on a buttons with gradient in the sidebar. Also, you can edit gradient by clicking on pencil button in left side of gradient buttons (button should be hovered).

![](images/applying_gradient.gif?raw=true)
package data.fractal.newtonfunction

import data.fractal.Complex

class SinZminus1 : NewtonFunctionBase() {
    override fun calculate(z: Complex): Complex {
        return z.sin() - 1.0
    }

    override fun derivative(z: Complex): Complex {
        return z.cos()
    }

    override val roots: List<Complex> = listOf(
        Complex(real = -4.712387462906845, img = 0.0),
        Complex(real = 14.137168768848055, img = 0.0),
        Complex(real = 7.85398094576645, img = 0.0),
        Complex(real = 1.57079507141702, img = 0.0),
        Complex(real = 20.420350608567688, img = 0.0),
        Complex(real = 598.4733987896009, img = 0.0),
        Complex(real = -10.99557282340898, img = 0.0),
        Complex(real = -23.561943375752207, img = 0.0),
        Complex(real = -36.128317305866176, img = 0.0),
        Complex(real = -17.27875856178437, img = 0.0),
        Complex(real = -29.845131343232648, img = 0.0),
        Complex(real = 26.70353638638441, img = 0.0),
        Complex(real = 64.40265046270741, img = 0.0),
        Complex(real = -42.41149956499947, img = 0.0),
        Complex(real = -48.69468716973352, img = 0.0),
        Complex(real = -54.97787330934729, img = 0.0),
        Complex(real = -61.26105613868598, img = 0.0),
        Complex(real = -67.54424100044068, img = 0.0),
        Complex(real = -73.82742721592392, img = 0.0),
        Complex(real = -80.1106113476146, img = 0.0),
        Complex(real = -86.39379619597135, img = 0.0),
        Complex(real = -92.67698327282734, img = 0.0),
        Complex(real = -117.80972284120503, img = 0.0),
        Complex(real = -98.96016759013234, img = 0.0),
        Complex(real = -111.52653949387233, img = 0.0),
        Complex(real = -142.9424648270241, img = 0.0),
        Complex(real = -155.50883763047472, img = 0.0),
        Complex(real = -105.24335322074506, img = 0.0),
        Complex(real = -130.3760944647551, img = 0.0),
        Complex(real = -124.09291071562083, img = 0.0),
        Complex(real = -186.92476181731567, img = 0.0),
        Complex(real = -218.34069042492487, img = 0.0),
        Complex(real = -224.62387598438545, img = 0.0),
        Complex(real = -161.79202078202505, img = 0.0),
        Complex(real = -243.47343038602793, img = 0.0),
        Complex(real = -387.9866912635404, img = 0.0),
        Complex(real = -1663.4733088655155, img = 0.0),
        Complex(real = 32.98672179727718, img = 0.0),
        Complex(real = 39.269907841812156, img = 0.0),
        Complex(real = 114.66813012181788, img = 0.0),
        Complex(real = 403.6946554987733, img = 0.0),
        Complex(real = 171.2167978870558, img = 0.0),
        Complex(real = 51.836278360772724, img = 0.0),
        Complex(real = 45.553094558403075, img = 0.0),
        Complex(real = 89.53538947432618, img = 0.0),
        Complex(real = 70.68583565423353, img = 0.0),
        Complex(real = 76.96901942043593, img = 0.0),
        Complex(real = 711.5707374345377, img = 0.0),
        Complex(real = 120.95131617480182, img = 0.0),
        Complex(real = 139.80087111264922, img = 0.0),
        Complex(real = 215.1990979130633, img = 0.0),
        Complex(real = 290.59732174432816, img = 0.0),
        Complex(real = 108.38494605644108, img = 0.0),
        Complex(real = 95.8185775812577, img = 0.0),
        Complex(real = 58.11946276171311, img = 0.0),
        Complex(real = 83.25220368756341, img = 0.0),
        Complex(real = 158.650430296306, img = 0.0),
        Complex(real = 183.78316875981136, img = 0.0),
        Complex(real = 102.10176192453449, img = 0.0),
        Complex(real = 127.23450095641758, img = 0.0),
        Complex(real = 133.51768918808648, img = 0.0),
        Complex(real = 152.36724262473544, img = 0.0),
        Complex(real = 190.0663545353246, img = 0.0),
        Complex(real = 177.49998656760897, img = 0.0),

        )
}
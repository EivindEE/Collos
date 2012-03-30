// Original code can be found at http://labs.ideeinc.com/multicolr/

/* (Requires jQuery, search.js, base.js) */

/* -.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.- */

// A simple class for colors and color manipulation functions.
function Color(r, g, b)
{
    this.r = r;
    this.g = g;
    this.b = b;
}

// NOTE: the ColorWheel needs to convert the hue from degrees to a value on
// [0, 1].
Color.HSV = function(H, S, V)
{
    var R, G, B;
    if (S==0) 
    {       // HSV values = From 0 to 1
        R = V*255;     // RGB results = From 0 to 255
        G = V*255;
        B = V*255;
    }
    else
    {
        var var_h = H*6;
        var var_i = Math.floor( var_h );     //Or ... var_i = floor( var_h )
        var var_1 = V*(1-S);
        var var_2 = V*(1-S*(var_h-var_i));
        var var_3 = V*(1-S*(1-(var_h-var_i)));
        var var_r, var_g, var_b;
        if (var_i==0)      {var_r=V ;    var_g=var_3; var_b=var_1}
        else if (var_i==1) {var_r=var_2; var_g=V;     var_b=var_1}
        else if (var_i==2) {var_r=var_1; var_g=V;     var_b=var_3}
        else if (var_i==3) {var_r=var_1; var_g=var_2; var_b=V}
        else if (var_i==4) {var_r=var_3; var_g=var_1; var_b=V}
        else               {var_r=V;     var_g=var_1; var_b=var_2}
        R = Math.round(var_r*255);   //RGB results = From 0 to 255
        G = Math.round(var_g*255);
        B = Math.round(var_b*255);
    }

    return new Color(R,G,B);
}

Color.fromHexString = function(s)
{
    var r = parseInt(s.substring(0,2), 16);
    var g = parseInt(s.substring(2,4), 16);
    var b = parseInt(s.substring(4,6), 16);
    
    return new Color(r,g,b);
}

// Returns original color with brightness scaled by 'factor'.
Color.scale = function(color, factor)
{
    if (factor < 0) return new Color(0,0,0);
    var r = Math.min(255, color.r * factor);
    var g = Math.min(255, color.g * factor);
    var b = Math.min(255, color.b * factor);
    
    return new Color(r,g,b);
}

// Scales this color's brightness by 'factor'.
Color.prototype.scale = function(factor)
{
    if (factor < 0)
    {
        this.r = this.g = this.b = 0;
    }
    else
    {
        this.r = Math.min(255, this.r * factor);
        this.g = Math.min(255, this.g * factor);
        this.b = Math.min(255, this.b * factor);
    }    
    return this;
}

// Returns a hex string representing this color (rrggbb format).
Color.prototype.toHexString = function()
{
    var pad = function(s) {
        if (s.length < 2) 
			s = "0" + s;
		return s;
    }
    var nr = pad(parseInt(this.r).toString(16));
	var ng = pad(parseInt(this.g).toString(16));
	var nb = pad(parseInt(this.b).toString(16));

	return nr + ng + nb;
}

/* -.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.- */

/* ColorPalette represents the color palette widget used to select colors
 * for color search. It is tied to the interface via the palette_div parameter,
 * which is a jQuery object containing one element - the parent div of all
 * color palette elements.
 *
 * ColorPalette expects that palette_div will have the following hierarchy:
 *
 *  /
 *      /#color_palette_img
 */
function ColorPalette(palette_div)
{   
    this.current_display_color = "ffffff";

    // TODO: pass these as constructor parameters

    // parameters for color selection
    this.ROWS = 16;      // number of rows in palette
    this.COLS = 16;      // number of columns in palette
    this.HEIGHT = 10;    // height of each palette entry
    this.WIDTH = 10;     // width of each palette entry

    // colors for palette (hardcoded for now)
    this.COLORS = new Array(
        'fddce5', 'fadcec', 'f6daed', 'eedced', 'e4daee', 'dfe1f1', 'dfeefa', 'e1f5fa', 'e1f3f0', 'e2f1de', 'eaf3d9', 'f8f9dc', 'fefcdf', 'fff1dc', 'fee1dc', 'fcd4d7',
        'fac7d2', 'f5c7da', 'edc7dc', 'dfc7df', 'd0c4e3', 'cbcbe7', 'c9e1f6', 'cdecf5', 'ceeae5', 'cee6c9', 'd7eac3', 'f0f3c7', 'fcf6c9', 'ffe7c9', 'fdcec7', 'f9b8be',
        'f5a3b6', 'e7a6c1', 'd3a7c4', 'bfa5c4', 'ada3c7', 'a7aad0', 'a9c9ed', 'aed7ea', 'afd6d0', 'add1aa', 'bed69f', 'dfe8a4', 'fcf1a6', 'ffcfa5', 'f9aca5', 'f397a0',
        'ee839b', 'd689ae', 'bf8bb2', 'a387b2', '8a82b2', '848bba', '8ab7e1', '92cde1', '95c9be', '95c18d', 'a9c97f', 'd0da83', 'f9ea87', 'febd84', 'f38d84', 'ee7b82',
        'ea6a81', 'ce709e', 'b176a2', '8c6ea1', '716da1', '6b73a7', '73a1d2', '7fc1d9', '83bfb4', '83ba72', '9abe5e', 'c3d268', 'f8e36c', 'fcaa6b', 'ee796b', 'eb626d',
        'e84b6e', 'cb6092', 'a5689b', '81659b', '67669d', '596a9f', '638fc3', '73bdd5', '7dbca9', '7cb45e', '8aba49', 'bccd40', 'f6da46', 'f99c47', 'ec6449', 'e94e56',
        'e73863', 'cb4e8d', 'a26095', '7e6199', '5d629b', '4b669e', '5486bd', '6cbdd4', '75baa1', '73b15a', '80b74c', 'b4c93d', 'f6d727', 'f68e2b', 'e84b33', 'e73843',
        'e72653', 'cd498c', 'a05e92', '7b5f98', '53609a', '46639c', '4e80b8', '6abbd3', '71b99e', '6fb058', '7eb44d', 'adc43f', 'f1d41c', 'f18828', 'e7402d', 'de323c',
        'd6254b', 'c24188', '9d5990', '765a95', '4e5d98', '445e9a', '437eb1', '55b4c7', '69b498', '68ac59', '70af50', '9fc043', 'd5c427', 'd9802d', 'd73b2e', 'c83239',
        'ba2944', 'b13381', '914c8e', '6f4d8f', '494e8f', '405191', '3071a6', '29a5b4', '44ab8c', '48a85b', '61aa54', '8bb449', 'b7ac3e', 'b97438', 'b93530', 'a72d36',
        '95253c', '912a76', '863c89', '693e88', '403f85', '3a4286', '19668f', '008c92', '00957d', '0c9556', '429553', '77974b', '908d41', '956830', '952b29', '87212a',
        '79172a', '792464', '6e2e76', '513074', '382e71', '303870', '214e77', '017079', '007a67', '107b48', '3b7b45', '657b42', '77733d', '795025', '77221d', '651c21',
        '541724', '591044', '511f59', '3c1d59', '231d57', '212257', '0e3b59', '01535a', '015c45', '205c31', '335c2d', '475c2b', '5a5527', '593d1c', '541f0e', '431716',
        '33101d', '331728', '2e1435', '241033', '1c1733', '101933', '082335', '023035', '033829', '1a3921', '233a21', '2c3b20', '3a351f', '382510', '33140c', '230b09',
        '140306', '14040c', '140817', '0c0614', '080614', '060814', '040a14', '031014', '03140e', '081a0c', '0e1c0a', '141c0a', '191909', '170e06', '140604', '140604',
        'ffffff', 'eeeeee', 'dddddd', 'cccccc', 'bbbbbb', 'aaaaaa', '999999', '888888', '777777', '666666', '555555', '444444', '333333', '222222', '111111', '000000'
    );

    // setup picker div and its mouse handlers
    this.palette_div = palette_div;

    // setup palette and its mouse handlers
    this.color_palette = this.palette_div.children("#palette0img");
    this.color_palette.bind('click', { 'handler' : this }, function(event) {
        event.preventDefault();
        rel_coords = get_coords(event, 'relative');
        event.data.handler.receive_palette_click(rel_coords[0], rel_coords[1]);
    });
}

/* Given the coordinates of a click on the image, get the
 * corresponding color and add it to the selected color list.
 */
ColorPalette.prototype.set_palette_color_for = function(x, y)
{
    // calculate color from click position
    var col = parseInt((x - 1)/this.WIDTH);
    var row = parseInt((y - 1)/this.HEIGHT);
    var index = row * this.COLS + col;
    // set display color
    this.current_display_color = this.COLORS[index];
//    alert(String(this.current_display_color));
    // add to colour selection
    //add_color(this.current_display_color);
}

ColorPalette.prototype.receive_palette_click = function(x, y)
{
    this.set_palette_color_for(x, y);
}

function get_coords(e, position) {
    var x, y;

    var IE = document.all?true:false;

    if(IE && position == 'absolute') {
	x = event.clientX + document.body.scrollLeft;
        y = event.clientY + document.body.scrollTop;
        return Array(x, y);
    }

    if (document.layers) {
        x = e.layerX;
        y = e.layerY;
    } else if (document.all) {
        x = event.offsetX;
        y = event.offsetY;// + document.body.scrollTop; // mac IE fix
    } else if (document.getElementById) {
        var x_off = 0; var y_off = 0;
        if (position == 'relative') {
            var pos = get_position(e.target);
            x_off = pos[0]; y_off = pos[1];
        }
        x = (e.pageX - x_off);
        y = (e.pageY - y_off);
    }
    return Array(x, y);
}

/* Returns the page position of the element 'obj'.
 * See http://blog.firetree.net/2005/07/04/javascript-find-position/
 * for details.
 */
function get_position(obj) {
    var x = 0, y = 0;
    if (obj.offsetParent) {
        while (true) {
            if (obj.offsetParent) {
                x += obj.offsetLeft;
                y += obj.offsetTop;
                obj = obj.offsetParent;
            }
            else break;
        }
    }
    else if (obj.x && obj.y) {
        x = obj.x;
        y = obj.y;
    }
    return Array(x, y);
}
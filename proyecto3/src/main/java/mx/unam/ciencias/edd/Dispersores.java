package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
		int l = llave.length;
		int r = 0;
		int i = 0;
		while(l >= 4) {
		    r ^= bigEndian(llave[i++], llave[i++], llave[i++], llave[i++]);
	    	l -= 4;
		}
		int t = 0;
		switch(l) {
		case 3: t |= (0xFF & llave[i+2]) << 8;
		case 2: t |= (0xFF & llave[i+1]) << 16;
		case 1: t |= (0xFF & llave[i]) << 24;
		}
		return r^t;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
		int l = llave.length;
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;
		for(int i = 0; i <= l; i+= 12) {
			// a
			byte d = (i < l) ? llave[i] : 0;
			byte e = (i+1 < l) ? llave[i+1] : 0;
			byte f = (i+2 < l) ? llave[i+2] : 0;
			byte g = (i+3 < l) ? llave[i+3] : 0;
			a += littleEndian(d, e, f, g);
			// b
			d = (i+4 < l) ? llave[i+4] : 0;
			e = (i+5 < l) ? llave[i+5] : 0;
			f = (i+6 < l) ? llave[i+6] : 0;
			g = (i+7 < l) ? llave[i+7] : 0;
			b += littleEndian(d, e, f, g);
			// c
			d = (i+8 < l) ? llave[i+8] : 0;
			e = (i+9 < l) ? llave[i+9] : 0;
			f = (i+10 < l) ? llave[i+10] : 0;
			g = (i+11 < l) ? llave[i+11] : 0;
			if(l - (i+8) >= 4) 
				c += littleEndian(d, e, f, g);
			else {
				c += l;
				c += littleEndian((byte) 0, d, e, f);
	    	}
			// mezcla(a, b, c);
			// 1
			a -= b + c;
			a ^= (c >>> 13);
			b -= c + a;
			b ^= (a << 8);
			//b ^= ((a & 0xFF) << 8);
			c -= a + b;
			c ^= (b >>> 13);
			//c ^= ((b & 0xFF) << 13);
			// 2
			a -= b + c;
			a ^= (c >>> 12);
			b -= c + a;
			b ^= (a << 16);
			//b ^= ((a & 0xFF) << 16);
			c -= a + b;
			c ^= (b >>> 5);
			//c ^= ((b & 0xFF) << 5);
			// 3
			a -= b + c;
			a ^= (c >>> 3);
			b -= c + a;
			b ^= (a << 10);
			//b ^= ((a & 0xFF) << 10);
			c -= a + b;
			c ^= (b >>> 15);
			//c ^= ((b & 0xFF) << 15);
        }
        return c;
    }
    
    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
		int h = 5381;
		for (int i = 0; i < llave.length; i++)
			h += (h << 5) + (0xFF & llave[i]);
		return h;
    }

    /**
     * Combina cuatro bytes en un entero de 32 bits en el esquema big-endian.
     * @param a el primer byte.
     * @param b el segundo byte.
     * @param c el tercer byte.
     * @param d el cuarto byte.
     * @return un entero de 32 bits.
     */
    private static int bigEndian(byte a, byte b, byte c, byte d) {
		return (a << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    /**
     * Combina cuatro bytes en un entero de 32 bits en el esquema little-endian.
     * @param a el primer byte.
     * @param b el segundo byte.
     * @param c el tercer byte.
     * @param d el cuarto byte.
     * @return un entero de 32 bits.
     */
    private static int littleEndian(byte a, byte b, byte c, byte d) {
		return (d << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | ((a & 0xFF));
    }
}

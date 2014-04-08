/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author Sail3
 */
public class Lienzo extends JComponent{
    /*inicio contastes creadas*/
    public static final int LINEA = 0;
    public static final int CIRCULO = 1;
    private int figura = 0;
    private Dimension circulo;
    private Dimension linea;
    private Color color;
    private final MedidaElemento medidaElemento = new MedidaElemento();
    /*fin constantes creadas*/
    
    
    private BufferedImage imagen;
    private EventosMouse eventosMouse = new  EventosMouse();
    public Lienzo() {
        setBackground(Color.red);
        setPreferredSize(new Dimension(300, 300));
        addMouseMotionListener(eventosMouse);
        addMouseListener(eventosMouse);
        color = (Color.RED);
    }
    public BufferedImage getImagen() {
        int ancho = Math.min(2000, getWidth());
        int alto = Math.min(2000, getHeight());
        if (imagen == null || ancho != imagen.getWidth() || alto != imagen.getHeight()) {
            int anchoNuevo = (imagen == null) ? ancho : Math.max(ancho, imagen.getWidth());
            int altoNuevo = (imagen == null) ? alto : Math.max(alto, imagen.getHeight());
            if (imagen != null && anchoNuevo > ancho && altoNuevo > alto) {
                return imagen;
            }
            imagen = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D g = imagen.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, ancho, alto);
            g.drawRenderedImage(imagen, AffineTransform.getTranslateInstance(0, 0));
            g.dispose();
            setPreferredSize(new Dimension(ancho, alto));
        }
        return imagen;
    }

    public Dimension getCirculo() {
        return circulo;
    }

    public void setCirculo(Dimension circulo) {
        this.circulo = circulo;
        this.medidaElemento.repaint();
    }

    public Dimension getLinea() {
        return linea;
    }

    public void setLinea(Dimension linea) {
        this.linea = linea;
        this.medidaElemento.repaint();
    }

    public int getFigura() {
        return figura;
    }

    public void setFigura(int figura) {
        this.figura = figura;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        medidaElemento.repaint();
    }
    
    public void limpiar() {
        imagen = null;
        repaint();
    }

    public MedidaElemento getMedidaElemento() {
        return medidaElemento;
    }
    
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRenderedImage(getImagen(), AffineTransform.getTranslateInstance(0, 0));   
    }
    
    public class EventosMouse extends MouseAdapter implements MouseMotionListener{
        private int X = 0;
        private int Y =0;
        
        private void pintarLinea(Point point) {
            Graphics2D g = getImagen().createGraphics();
            g.setColor(getColor());
            g.drawLine(X, Y, point.x, point.y);
            repaint();
        }
        private void pintarLinea(Point point, int ancho) {
            Graphics2D g = getImagen().createGraphics();
            g.setColor(getColor());
            int diferenciaX = 0;
            int diferenciaY = 0;
            if (Y == point.y) {
                diferenciaY = linea.height;
            }
            if (X == point.x) {
                diferenciaX = linea.width;
            }
            if (X < point.x) {
                if (Y < point.y) {
                    diferenciaX = linea.width;
                    diferenciaY = - linea.height;
                }
                if (Y > point.y) {
                    diferenciaX = linea.width;
                    diferenciaY = linea.height;
                }
            }
            if (X > point.x) {
                if (Y < point.y) {
                    diferenciaX = linea.width;
                    diferenciaY = linea.height;
                }
                if (Y > point.y) {
                    diferenciaX = -linea.width;
                    diferenciaY = linea.height;
                }
            }
            int [] xs = {X, X+ diferenciaX, point.x + diferenciaX, point.x};
            int [] ys = {Y, Y + diferenciaY, point.y + diferenciaY,point.y};
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillPolygon(xs, ys, 4);
            g.dispose();
            repaint();
        }
        private void pintarCirculo(Point point) {
            int diametro = circulo.height;
            int radio = diametro / 2;
            Graphics2D g = imagen.createGraphics();
            g.setColor(getColor());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillOval(point.x - radio, point.y - radio, diametro, diametro);
            
            g.dispose();
            repaint();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            switch (figura) {
                case LINEA:
                    Point point = e.getPoint();
                    pintarLinea(point, 12);
                    break;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch (figura) {
                case LINEA:
                    Point p = e.getPoint();
                    X = p.x;
                    Y = p.y;
                    break;
                case CIRCULO:
                    pintarCirculo(e.getPoint());
                    break;
                default:
                    throw new AssertionError();
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            switch (figura) {
                case CIRCULO:
                    pintarCirculo(e.getPoint());
                    break;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            /*
            Graphics2D g2d = getImage().createGraphics();
            g2d.setColor(Color.red);
            g2d.drawRect(10, 10, 100, 100);
            g2d.drawLine((int)(Math.random() * getWidth()), (int)(Math.random() * getHeight()), (int)(Math.random() * getWidth()), (int)(Math.random() * getHeight()));
            repaint();*/
        }
        
    }
    
    public class MedidaElemento extends JComponent {

        @Override
        public Dimension getPreferredSize() {
             return new Dimension(40, 40);
        }

        @Override
        public void paint(Graphics g) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getColor());
            switch (getFigura()) {
                case CIRCULO:
                    int radio = circulo.width / 2;
                    int diametro = circulo.height;
                    g.fillOval(0, 0, diametro, diametro);
                    break;
                case LINEA:
                    g.fillRect(0, 0, linea.width, linea.height);
                    break;
            }
        }
    }
}

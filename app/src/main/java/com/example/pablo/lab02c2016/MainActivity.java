package com.example.pablo.lab02c2016;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    DecimalFormat f = new DecimalFormat("##.00");

    //AGREGADO (3.B)
    ToggleButton toggleSeleccion;
    Spinner spinnerHora;
    Switch switchHora;
    TextView textviewPedido;
    RadioGroup radioGroup;
    RadioButton radioPlato;
    RadioButton radioPostre;
    RadioButton radioBebida;
    Button btnAgregar;
    Button btnConfirmar;
    Button btnReiniciar;
    ListView listviewLista;
    Boolean pedidoConfirmado;
    private ArrayList<ElementoMenu> elementosPedidos;
    Double total;

    // AGREGADO (3.C)
    private ElementoMenu[] listaBebidas;
    private ElementoMenu[] listaPlatos;
    private ElementoMenu[] listaPostre;
    ElementoMenu elementoActual;

    private ArrayAdapter<ElementoMenu> listaElementos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarListas();
        toggleSeleccion = (ToggleButton) findViewById(R.id.toggle_seleccion);
        spinnerHora = (Spinner) findViewById(R.id.spinner_hora);
        switchHora = (Switch) findViewById(R.id.switch_hora);
        textviewPedido = (TextView) findViewById(R.id.textView_pedido);
        radioGroup = (RadioGroup) findViewById(R.id.rBtnGroup);
        radioPlato = (RadioButton) findViewById(R.id.radioButton_plato);
        radioPostre = (RadioButton) findViewById(R.id.radioButton_postre);
        radioBebida = (RadioButton) findViewById(R.id.radioButton_bebida);
        btnAgregar = (Button) findViewById(R.id.button_agregar);
        btnConfirmar = (Button) findViewById(R.id.button_confirmarpedido);
        btnReiniciar = (Button) findViewById(R.id.button_reiniciar);
        listviewLista = (ListView) findViewById(R.id.listView_lista);
        pedidoConfirmado = false;
        total = (Double) 0.0;
        elementosPedidos = new ArrayList<ElementoMenu>();

        //Definimos la lista de productos que se ofrecen y lo seteamos al Adaptador
        listaElementos = new ArrayAdapter<ElementoMenu>(this,android.R.layout.simple_list_item_single_choice);
        listviewLista.setAdapter(listaElementos);
        listviewLista.setChoiceMode(listviewLista.CHOICE_MODE_SINGLE);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                listviewLista.clearChoices();
                switch (i){
                    case -1:
                        break;
                    case R.id.radioButton_plato:
                        listaElementos.clear();
                        listaElementos.addAll(Arrays.asList(listaPlatos));

                        break;
                    case R.id.radioButton_bebida:
                        listaElementos.clear();
                        listaElementos.addAll(Arrays.asList(listaBebidas));
                        break;
                    case R.id.radioButton_postre:
                        listaElementos.clear();
                        listaElementos.addAll(Arrays.asList(listaPostre));
                        break;
                    default:
                        break;
                }
            }
        });

        listviewLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                elementoActual = (ElementoMenu) listviewLista.getItemAtPosition(i);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!radioBebida.isChecked() && !radioPostre.isChecked() && !radioPlato.isChecked()){
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.toast1),Toast.LENGTH_SHORT).show();
                }
                else if(pedidoConfirmado)
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.toast2),Toast.LENGTH_SHORT).show();
                else{
                    textviewPedido.setText(textviewPedido.getText() + "\n" + elementoActual.toString());
                    elementosPedidos.add(elementoActual);
                    total += elementoActual.getPrecio();
                }

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(textviewPedido.equals(""))
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast3), Toast.LENGTH_SHORT).show();
                else if(!pedidoConfirmado) {
                    pedidoConfirmado = true;
                    textviewPedido.setText(textviewPedido.getText() + "\nTotal: $" + f.format(total));
                }
                else
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast4), Toast.LENGTH_SHORT).show();
        }
        });
        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup.check(-1);
                listaElementos.clear();
                elementosPedidos.clear();
                textviewPedido.setText("");
                pedidoConfirmado = false;
                listviewLista.clearChoices();
            }
        });
        String[] listaHora = new String[5];

        listaHora[0]=getResources().getString(R.string.hora1);
        listaHora[1]=getResources().getString(R.string.hora2);
        listaHora[2]=getResources().getString(R.string.hora3);
        listaHora[3]=getResources().getString(R.string.hora4);
        listaHora[4]=getResources().getString(R.string.hora5);
        ArrayAdapter<String> hora = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,listaHora);
        spinnerHora.setAdapter(hora);
        toggleSeleccion.setText(getResources().getString(R.string.TextToggle1));
        toggleSeleccion.setTextOff(getResources().getString(R.string.TextToggle1));
        toggleSeleccion.setTextOn(getResources().getString(R.string.TextToggle2));


    }

    //AGREGADO (3.A)
    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i,n,0.0);
            Random r = new Random();
            this.precio= (r.nextInt(3)+1)*((r.nextDouble()*100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre+ "( "+f.format(this.precio)+")";
        }
    }

    //AGREGADO (3.D)
    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[12]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");

    }


}

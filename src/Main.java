import com.itextpdf.commons.exceptions.ITextException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import static com.itextpdf.kernel.geom.PageSize.A4;


public class Main {
    private String pathJson = "Data/data.json";
    private String pathCsv = "Data/data.csv";
    static JDialog modelDialog = new JDialog();

    public void initilisation(){
        try{
            FileInputStream InputStream = new FileInputStream(this.pathCsv);
            int counter = 0;
            Scanner scan = new Scanner(InputStream);
            List<Film> films = new ArrayList<Film>();

            while (scan.hasNextLine()){
//                System.out.println(scan.hasNextInt());
                String[] movie = scan.nextLine().split(";");
                if (counter != 0){
//                        System.out.println(movie.length);
                    Film film = new Film(cleanString(movie[0]), cleanString(movie[1]), cleanString(movie[2]), cleanString(movie[3]));
//                    films.add(film);

                    this.writeJson(film);
                }
                counter++;
            }

        }catch (FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void writeJson(Film movie){
        try{
            JSONArray movieJson = this.readJson();
            movieJson.add(movie.toJson());

            FileWriter fw = new FileWriter(this.pathJson);
            fw.write(movieJson.toJSONString());
            fw.flush();
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private String cleanString(String str){
        return str.replaceAll("\"", "");
    }

    public JSONArray readJson(){
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        try{
            FileReader fr = new FileReader(this.pathJson); // lecture du fichier
            jsonArray = (JSONArray) jsonParser.parse(fr);
        } catch (IOException e){
            System.out.println(e.getMessage());
        } catch (ParseException pe){
            System.out.println(pe.getMessage());
            return jsonArray;
        }
        return jsonArray;
    }

    public Object[][] showDataJson(){
        List<Object[]> data = new ArrayList<Object[]>();
        JSONArray json = this.readJson();
        json.forEach( film -> {
            JSONObject f = (JSONObject) film;

            Object[] ff = {f.get("titre"),
                    f.get("duree"), f.get("genre"), f.get("sysnopsis")};
            data.add(ff);
        });
        Object[][] skull = new Object[data.size()][];
        for (int i=0; i < skull.length; i++){
            skull[i] = data.get(i);
        }

        return skull;
    }

    public void windows(){
        JFrame frame = new JFrame("Mes films");
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(Color.GRAY);
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.X_AXIS));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel titlemenu = new JLabel();
        titlemenu.setText("Menu :");
        titlemenu.setForeground(Color.BLACK);
        titlemenu.setToolTipText("ceci est le menu");

        JButton btn = new JButton("hello");
        JButton btnPrint = new JButton("Imprimer");

        btnPrint.addActionListener(new ActionPrint());
        btn.addActionListener(new ActionBtn());
        final JDialog modalJDialog =createDialog(frame);

        panelMenu.add(titlemenu);
        panelMenu.add(btn);

        JScrollPane tablePanel = new JScrollPane();
        String[] columnName = {"Titre", "Genre", "Durée", "Synopsys"};
        Object[][] data = this.showDataJson();

        JTable table = new JTable(data, columnName);
        tablePanel.add(table);

        Container tableContainer = new Container();
        tableContainer.setLayout(new BorderLayout());
        tableContainer.add(table.getTableHeader(), BorderLayout.PAGE_START);
        tableContainer.add(table, BorderLayout.CENTER);

//        Container form = new Container();
//        JLabel labelmovie = new JLabel("Enter the name of a movie");
//        JTextField moviefield = new JTextField(40);
//        moviefield.addActionListener(new TextFieldMovie());
//        panelMenu.add(labelmovie, BorderLayout.PAGE_END);
//        panelMenu.add(moviefield, BorderLayout.PAGE_END);


        table.setFillsViewportHeight(true);

        frame.getContentPane().add(BorderLayout.NORTH, panelMenu);
        frame.getContentPane().add(BorderLayout.CENTER, tableContainer);
//        frame.getContentPane().add(BorderLayout.SOUTH, form);


        // comportement à la fermeture de la window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }catch (InstantiationException e){
            throw new RuntimeException(e);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }catch (UnsupportedLookAndFeelException e){
            throw new RuntimeException(e);
        }
        frame.setSize(600,600);
        frame.setVisible(true);

    }

    private class ActionBtn implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            modelDialog.setVisible(true);
        }
    }

    private class ActionPrint implements ActionListener{
        private String pdfPath = "Data/data.pdf";

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                PdfWriter writer = new PdfWriter(this.pdfPath);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument, A4);
                document.setMargins(2.5f,2.5f,2.5f,2.5f);
                PdfPage page = pdfDocument.addNewPage();
                PdfCanvas pdfCanvas = new PdfCanvas(page);
//                Graphics2D g2 =
                Canvas canvas = new Canvas(pdfCanvas,pdfDocument, table.print());

            }catch(ITextException | FileNotFoundException ed) {
                throw new RuntimeException(ed.getMessage());
            }

        }
    }
    private JDialog createDialog(final JFrame frame){
        modelDialog = new JDialog(frame, "ma modal",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setBounds(250,250,300,200);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());
        dialogContainer.add(new JLabel("c'est super"), BorderLayout.CENTER);

        JLabel labelmovie = new JLabel("Enter the name of a movie");
        JTextField moviefield = new JTextField(40);
        moviefield.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("The entered text is: " + moviefield.getText());
            }
        });



        dialogContainer.add(labelmovie, BorderLayout.NORTH);
        dialogContainer.add(moviefield, BorderLayout.CENTER);

        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        JButton close = new JButton("ok");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelDialog.setVisible(false);
            }
        });
        pan.add(close);
        dialogContainer.add(pan,BorderLayout.SOUTH);

        return modelDialog;
    }

    public static void main(String[] args) {

        Main ma = new Main();
        JSONArray Ja = ma.readJson();
        if(Ja.isEmpty()){
            ma.initilisation();
        }
        ma.windows();

//        Film newfilm = new Film("Le retour du roi","Heroic fantaisy","3h00","le roi est la");
//        ma.writeJson(newfilm);


//        System.out.println("list ou numero ?");
//        Scanner scanner = new Scanner(System.in);
//        String age = scanner.nextLine();





//            try {

//                Scanner question = new Scanner(System.in);
//                System.out.println("Veuillez saisir List pour l'ensemble des films ou un numero de 1 à "+films.size());
//                String response = question.nextLine();
//                if(response.equals("list")){
//                    ListIterator iterator = films.listIterator();
//                    while (iterator.hasNext()){
//                        Film fil = (Film) iterator.next();
//                        System.out.println(fil.toString());
////                        iterator.next().toString();
//                    }
//                }else{
//                    System.out.println(films.get(Integer.parseInt(response) -1).toString());
//                }

//            }catch (FileNotFoundException e){
//                throw new RuntimeException(e.getMessage());
//            }

    }
}
package Bean;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class mBeanPDF {
    private Document doc = null;
    private OutputStream os = null;
    private StreamedContent file;
    
    @PostConstruct
    public void init(){
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/pdf/documento.pdf");
        file = new DefaultStreamedContent(stream, "document/pdf", "pdf.pdf");
    }

    public StreamedContent getFile() {
        System.out.println("FILE - "+file.getName());
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
    public void gerarPDF(){    
        try {
            //ABRINDO DOCUMENTO E DEFININDO O ESTILO E AS MARGENS
            doc = new Document(PageSize.A4, 30, 20, 72, 72);
            //PEGANDO CAMINHO DO DOCUMENTO PDF JÁ CRIADO
            String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/resources/pdf/documento.pdf"; 
            //PEGANDO CAMINHO DA IMAGEM QUE SERÁ ADICIONADA AO PDF
            String caminho2 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/resources/img/if.png";           
            os = new FileOutputStream(caminho);
            PdfWriter.getInstance(doc, os);
            doc.open();
            
            //ESCREVENDO NO PDF
            //ADICIONANDO LOGO DO IF
            Image img = Image.getInstance(caminho2);
            img.setAbsolutePosition(10f, 700f);            
            img.setAlignment(Element.ALIGN_LEFT);
            doc.add(img);

            //TEXTO
            //CRIANDO UM PARÁGRAFO. 'TIMES_BOLD' É REFERENTE AO TIPO DA FONTE E '22' É O TAMANHO DA FONTE
            Paragraph p = new Paragraph("Bem vindo!",FontFactory.getFont(FontFactory.TIMES_BOLD, 22));
            //DEFININDO ALINHAMENTO DO PARÁGRAFO
            p.setAlignment(Element.ALIGN_CENTER);
            //ADICIONANDO PARÁGRAFO AO DOCUMENTO
            doc.add(p);
            
            p = new Paragraph("\n\nMeu primeiro parágrafo PDF!",FontFactory.getFont(FontFactory.COURIER, 12));
            p.setSpacingAfter(20);
            doc.add(p);
            p = new Paragraph("Meu segundo parágrafo PDF!",FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
            doc.add(p);
            
            //PARA CRIAR UM PARÁGRAFO COM MAIS DE UM ESTILO DE FONTE, É NECESSÁRIO DIVIDI-LO EM FRASES
            p = new Paragraph("Meu terceiro parágrafo PDF! Autor: ",FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
            //CRIANDO FRASE
            Phrase phrase1 = new Phrase("José Gerardo",FontFactory.getFont(FontFactory.TIMES_BOLD, 12));
            //ADICIONANDO AO PARÁGRAFO
            p.add(phrase1);
            doc.add(p);     
                        
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(mBeanPDF.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if (doc != null) {
                doc.close();
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

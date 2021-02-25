/**
 * Created by ASUS on 2017/11/17.
 */
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;

import javax.swing.tree.ExpandVetoException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple filling out form example.
 */
public class C05E02_FillAndModifyForm {

    public static final String SRC = "src/main/resources/pdf/job_application.pdf";
    public static final String SRC2 = "src/main/resources/pdf/88th_noms_announcement.pdf";
    public static final String DEST = "results/chapter05/filled_out_job_application.pdf";
    public static final String DEST_MERGE = "results/chapter05/filled_out_job_application_merged.pdf";

    public static Map<String, String> articleMapOne;
    static {
        articleMapOne = new HashMap<>();
        articleMapOne.put("name", "Felipe Martins");
        articleMapOne.put("language", "Portuguese");
    }

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C05E02_FillAndModifyForm().manipulatePdf2(articleMapOne, DEST);
    }

    public void manipulatePdf2(Map<String, String> articleMapOne, String dest) throws IOException {
        try {
            //Initialize PDF document
            // IE workaround: write into byte array first.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Apply preferences and build metadata.
            Document document = new Document();
            PdfWriter writer1 = new PdfWriter(dest);
            PdfDocument writer = new PdfDocument(writer1);
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), pdfWriter);
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));


            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            final Map<String, PdfFormField> fields = form.getFormFields();

            articleMapOne.forEach((k, v) -> fields.get(k).setValue(v));

//        fields.get("name").setValue("James Bond");
//        fields.get("language").setValue("English");

            fields.get("experience1").setValue("Yes");
            fields.get("experience2").setValue("Yes");
            fields.get("experience3").setValue("Yes");

            System.out.println("Done!!!!!!!!!!");

//            PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
//            PdfMerger merger = new PdfMerger(pdf);
//
////            PdfMerger merger = new PdfMerger(pdfDoc);
////            merger.merge(pdf, 1, pdfDoc.getNumberOfPages());
//            merger.merge(pdf, 1, 1);
//
//            //Add pages from the second pdf document
//            PdfDocument secondSourcePdf = new PdfDocument(new PdfReader(SRC2));
//            merger.merge(secondSourcePdf, 1, secondSourcePdf.getNumberOfPages());

            pdfDoc.close();
//            secondSourcePdf.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void manipulatePdf(String src, String dest) throws IOException {

        //Initialize PDF document
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));


        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();

        fields.get("name").setValue("James Bond").setBackgroundColor(Color.ORANGE);
        fields.get("language").setValue("English");

        fields.get("experience1").setValue("Yes");
        fields.get("experience2").setValue("Yes");
        fields.get("experience3").setValue("Yes");

        List<PdfString> options = new ArrayList<PdfString>();
        options.add(new PdfString("Any"));
        options.add(new PdfString("8.30 am - 12.30 pm"));
        options.add(new PdfString("12.30 pm - 4.30 pm"));
        options.add(new PdfString("4.30 pm - 8.30 pm"));
        options.add(new PdfString("8.30 pm - 12.30 am"));
        options.add(new PdfString("12.30 am - 4.30 am"));
        options.add(new PdfString("4.30 am - 8.30 am"));
        PdfArray arr = new PdfArray(options);
        fields.get("shift").setOptions(arr);
        fields.get("shift").setValue("Any");

        PdfFont courier = PdfFontFactory.createFont(FontConstants.COURIER);
        fields.get("info").setValue("I was 38 years old when I became an MI6 agent.", courier, 7f);

        pdfDoc.close();

    }
}
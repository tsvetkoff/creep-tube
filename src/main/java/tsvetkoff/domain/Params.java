package tsvetkoff.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import tsvetkoff.creep.strategy.factory.StrategyTypeValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Params {

    public double dr, dt, t_max;
    public double R1, R2;
    public double mu, E;
    public double c_gamma, c_p, m, b, n, lambda, mu_1;
    public double A1_p, mA_p, A1_gamma, mA_gamma;
    public double alpha1_gamma, mAlpha_gamma, alpha1_p, mAlpha_p;
    public double sigma_0, tau_max, q;
    // моменты времени для отображения
    public TreeSet<Double> stressTimes;

    public Params(String file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            SAXBuilder parser = new SAXBuilder();
            Document document = parser.build(reader);
            Element root = document.getRootElement();

            dt = Double.parseDouble(root.getChild("calculation").getChild("dt").getAttributeValue("value"));
            dr = Double.parseDouble(root.getChild("calculation").getChild("dr").getAttributeValue("value"));
            t_max = Double.parseDouble(root.getChild("calculation").getChild("t_max").getAttributeValue("value"));
            sigma_0 = Double.parseDouble(root.getChild("calculation").getChild("sigma_0").getAttributeValue("value"));
            tau_max = Double.parseDouble(root.getChild("calculation").getChild("tau_max").getAttributeValue("value"));
            q = Double.parseDouble(root.getChild("calculation").getChild("q").getAttributeValue("value"));

            R1 = Double.parseDouble(root.getChild("geometry").getChild("R1").getAttributeValue("value"));
            R2 = Double.parseDouble(root.getChild("geometry").getChild("R2").getAttributeValue("value"));
            if (q != 0 && R1 == 0) {
                throw new IllegalArgumentException("Internal pressure is not 0 but R1 is 0!");
            }

            E = Double.parseDouble(root.getChild("elasticity").getChild("E").getAttributeValue("value"));
            mu = Double.parseDouble(root.getChild("elasticity").getChild("mu").getAttributeValue("value"));

            n = Double.parseDouble(root.getChild("creep").getChild("v").getChild("n").getAttributeValue("value"));
            b = Double.parseDouble(root.getChild("creep").getChild("v").getChild("b").getAttributeValue("value"));
            lambda = Double.parseDouble(root.getChild("creep").getChild("v").getChild("lambda").getAttributeValue("value"));
            mu_1 = Double.parseDouble(root.getChild("creep").getChild("v").getChild("mu_1").getAttributeValue("value"));

            m = Double.parseDouble(root.getChild("creep").getChild("w").getChild("m").getAttributeValue("value"));
            c_p = Double.parseDouble(root.getChild("creep").getChild("w").getChild("c").getAttributeValue("value"));
            mAlpha_p = Double.parseDouble(root.getChild("creep").getChild("w").getChild("alpha").getChild("mAlpha").getAttributeValue("value"));
            alpha1_p = Double.parseDouble(root.getChild("creep").getChild("w").getChild("alpha").getChild("alpha1").getAttributeValue("value"));
            mA_p = Double.parseDouble(root.getChild("creep").getChild("w").getChild("A").getChild("mA").getAttributeValue("value"));
            A1_p = Double.parseDouble(root.getChild("creep").getChild("w").getChild("A").getChild("A1").getAttributeValue("value"));

            initGammaConstants();

            List<Element> stressTimesList = root.getChild("output").getChild("stressTimes").getChildren("t");
            stressTimes = new TreeSet<>();
            for (Element timeElement : stressTimesList) {
                stressTimes.add(Double.parseDouble(timeElement.getAttributeValue("value")));
            }
        } catch (JDOMException e) {
            throw new IOException(e);
        }
    }

    public Params initGammaConstants() {
        c_gamma = c_p;
        mAlpha_gamma = mAlpha_p;
        alpha1_gamma = alpha1_p;
        mA_gamma = mA_p;
        A1_gamma = A1_p;
        return this;
    }
}

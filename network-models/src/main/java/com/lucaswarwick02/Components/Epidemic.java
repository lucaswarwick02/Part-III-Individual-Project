package com.lucaswarwick02.components;

import java.io.File;
import java.net.URL;

import com.lucaswarwick02.Main;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Epidemic {

    @XmlElement(name = "infectionRate")
    public double infectionRate;

    @XmlElement(name = "recoveryRate")
    public double recoveryRate;

    @XmlElement(name = "hospitalisationRate")
    public double hospitalisationRate;

    @XmlElement(name = "mortalityRate")
    public double mortalityRate;

    @Override
    public String toString() {
        return "Epidemic [infectionRate=" + infectionRate + ", recoveryRate=" + recoveryRate + ", hospitalisationRate="
                + hospitalisationRate + ", mortalityRate=" + mortalityRate + "]";
    }

    public void logInformation () {
        Main.LOGGER.info("Epidemic Parameters: ");
        Main.LOGGER.info("... infectionRate: " + infectionRate);
        Main.LOGGER.info("... recoveryRate: " + recoveryRate);
        Main.LOGGER.info("... hospitalisationRate: " + hospitalisationRate);
        Main.LOGGER.info("... mortalityRate: " + mortalityRate);
    }

    public static void saveExample(File file) {
        Epidemic epidemic = new Epidemic();

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Epidemic.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(epidemic, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Epidemic loadFromResources(String fileName) {
        Epidemic epidemic = new Epidemic();

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Epidemic.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            URL url = Main.class.getResource(fileName);
            File file = new File(url.toURI());

            epidemic = (Epidemic) jaxbUnmarshaller.unmarshal(file);

        } catch (Exception e) { e.printStackTrace(); }

        return epidemic;
    }
}

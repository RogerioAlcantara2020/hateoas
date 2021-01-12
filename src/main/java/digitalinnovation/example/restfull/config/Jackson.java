package digitalinnovation.example.restfull.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import digitalinnovation.example.restfull.enums.Raca;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Jackson {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
//        Objetos não mapeadas não quebram
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        Falha se alguma coisa vier vazia
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//        serve para compatibilidade de arrays, quado tem um arrau com um item, caso não tenha essa config ele perde
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//        Serelise datas
        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.registerModule(racaModuleMapper());

        return objectMapper;
    }

    public SimpleModule racaModuleMapper(){
        SimpleModule dataModule = new SimpleModule(JSONRacaModule, PackageVersion.VERSION);
        dataModule.addSerializer(Raca.class,new RacaCerialize());
        dataModule.addDeserializer(Raca.class,new RacaDescerialize());
        return dataModule;
    }

    class RacaCerialize extends StdSerializer<Raca>{
        public RacaCerialize(){
            super(Raca.class);
        }

        @Override
        public void serialize(Raca raca, JsonGenerator json, SerializerProvider provider) throws IOException {
            json.writeString(raca.getValue());
        }
    }

    class RacaDescerialize extends StdSerializer<Raca>{
        public RacaDescerialize(){
            super(Raca.class);
        }
        @Override
        public Raca deserialize(JsonParser p, DeserializationContext ctxt)throws IOException, JsonProcessingException{
            return Raca.of(p.getText());
        }
    }
}

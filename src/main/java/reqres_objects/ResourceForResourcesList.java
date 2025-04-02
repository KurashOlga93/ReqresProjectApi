package reqres_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ResourceForResourcesList {

        @Expose
        int id;

        @Expose
        String name;

        @Expose
        long year;

        @Expose
        String color;

        @SerializedName("pantone_value")
        @Expose
        String pantoneValue;
}

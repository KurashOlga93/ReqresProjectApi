package requres_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserForUsersList {

    @Expose
    int id;

    @Expose
    String email;

    @SerializedName("first_name")
    @Expose
    String firstName;

    @SerializedName("last_name")
    @Expose
    String lastName;

    @Expose
    String avatar;
}

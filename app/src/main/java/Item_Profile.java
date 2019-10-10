public class Item_Profile {

    String Item_Name;
    String Item_Price;
    String Item_Img;
    String Item_Detail;

    public Item_Profile(String Item_Name, String Item_Price, String Item_Img, String Item_Detail){
        this.Item_Name = Item_Name;
        this.Item_Price = Item_Price;
        this.Item_Img = Item_Img;
        this.Item_Detail = Item_Detail;
    }

    public String getItem_Name(){
        return Item_Name;
    }

    public String getItem_Price(){
        return Item_Price;
    }

    public String getItem_Img(){
        return Item_Img;
    }

    public String getItem_Detail(){
        return Item_Detail;
    }

}

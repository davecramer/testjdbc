/**
 * Created by davec on 2015-10-14.
 */
public class ShowArrayType
{
    public static void main(String []args)
    {
        int []intarr =  {1,2};
        Integer []integers = {1,2};


        System.out.println(intarr.getClass().getSimpleName());
        System.out.println(intarr.getClass().getComponentType().getSimpleName());

        System.out.println(integers.getClass().getSimpleName());
        System.out.println(integers.getClass().getComponentType().getSimpleName());
    }
}

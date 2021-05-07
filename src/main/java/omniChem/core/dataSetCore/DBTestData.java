package omniChem.core.dataSetCore;

public class DBTestData {

    public static Table GetTestData() {
        Table table = new Table();
        table.setName("Clients");

        DataPoint p = new DataPoint();
        p.setVarType("varchar");
        p.setName("client_name");

        DataPoint q = new DataPoint();
        q.setVarType("DOUBLE");
        q.setName("client_money");

        table.addAttribute(p);
        table.addAttribute(q);

        DataPoint T = p.clone();
        T.setValue("'James'");
        DataPoint C = q.clone();
        C.setValue("5.6");

        Entity ent = new Entity();
        ent.addAttribute(T);
        ent.addAttribute(C);
        table.addEntity(ent);

        return table;
    }

}

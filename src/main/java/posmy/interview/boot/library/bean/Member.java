package posmy.interview.boot.library.bean;

public class Member {
    private String name;
    private int age;
    private String address;
    private boolean isLogged;

    public Member(String name, int age, String address, boolean isLogged) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.isLogged = isLogged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", isLogged=" + isLogged +
                '}';
    }
}

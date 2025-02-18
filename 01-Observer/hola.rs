

trait Attach {
    fn attach(&self);
}

trait Detach {
    fn detach(&self);
}

trait Notify {
    fn notify(&self);
}

struct Observer {

}

struct Subject {
    observers : Vec<Observer>
};
//import LinkedListNode from './LinkedListNode.js';
const LinkedListNode = require('./LinkedListNode.js');

module.exports = class LinkedList {
    constructor () {
        this.head = null;
        this.tail = null;
    }

    insert (value) {
        let node = new LinkedListNode(value);
        if (this.head == null) {
            this.head = node;
            this.tail = node;
        } else {
            this.tail.next = node;
            this.tail = node;
        }
    }

    prepend (value) {
        let node = new LinkedListNode(value);

        node.next = head;
        this.head = node;

        if (this.tail == null) {
            this.tail = node;
        }
    }

    find (value) {
        let currentPoint = this.head;
        while (currentPoint != null && currentPoint.value != value) {
            currentPoint = currentPoint.next;
        }
        if (currentPoint == null) {
            return null;
        } else {
            return currentPoint.value;
        }
    }

    contains (value) {
        return this.find(value) != null;
    }

    remove (value) {
        if (this.head == null) return false;
        let head = this.head;
        if (head.value == value) {
            if (this.head == this.tail) {
                this.head = this.tail = null;
            } else {
                this.head = head.next;
            }
            return true;
        }
        while (head != null && head.next.value != value) {
            head = head.next;
        }
        if (head.next != null) {
            if (head.next == this.tail) {
                this.tail = head;
            }
            head.next = head.next.next;
            return true;
        }
        return false;
    }

    traverse () {
        let currentPoint = this.head;
        while (currentPoint != null) {
            console.log(currentPoint.value + ' -> ');
            currentPoint = currentPoint.next;
        }
        console.log(null);
    }
}

using Unity.VisualScripting;
using UnityEngine;

public class Bullet : MonoBehaviour
{
    [SerializeField]
    private float speed = 0.1f;
    void Start()
    {

    }

    void Update()
    {
        Movement();
    }

    void Movement()
    {
        transform.Translate(Vector2.up * speed);
        if (transform.position.y >= 11)
        {
            Destroy(this.gameObject);
        }
    }
    private void OnTriggerEnter2D(Collider2D other)
    {
        Destroy(this.gameObject);
    }
    private void OnTriggerStay2D(Collider2D other)
    {
        Destroy(this.gameObject);
    }
}

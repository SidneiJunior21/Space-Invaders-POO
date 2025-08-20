using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;

public class Player : MonoBehaviour
{
    public GameObject bullet;
    [SerializeField]
    private float speed = 0.05f;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {

    }
    // Update is called once per frame
    void Update()
    {
        Movement();
        Shoot();
    }

    void Movement()
    {
        if (Keyboard.current.dKey.IsPressed())
        {
            if (transform.position.x >= 16.7) { return; }
            transform.Translate(Vector2.right * speed);
        }
        if (Keyboard.current.aKey.IsPressed())
        {
            if (transform.position.x <= -16.7) { return; }
            transform.Translate(Vector2.left * speed);
        }
    }

    void Shoot()
    {
        if (Keyboard.current.spaceKey.wasPressedThisFrame)
        {
            Instantiate(bullet, transform.position, transform.rotation);
        }  
    }
}
